package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class UserDbStorage {
    private JdbcTemplate template;

    public User create(User user) {
        template.update(
                "insert into users (name, login, email, birthday) values(?, ?, ?, ?)",
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                Date.from(user.getBirthday().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Integer userId = template.queryForObject(
                "select max(id) as max from users", (rs, rowNum) -> rs.getInt("max"));
        user.setId(userId);
        log.info("Сохранить пользователя {}", userId);
        return user;
    }

    public User update(User user) {
        checkExistsById(user.getId());
        template.update(
                "update users set name = ?, login = ?, email = ?, birthday = ? where id = ?",
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                Date.from(user.getBirthday().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                user.getId());
        List<Integer> followersList = template.query(
                "select following_id from follows where followed_id = ?",
                ((rs, rowNum) -> rs.getInt("following_id")), user.getId());
        user.getFriends().addAll(followersList);
        log.info("Обновление пользователя {}", user.getId());
        return user;
    }


    public User getById(Integer id) {
        checkExistsById(id);
        User user = template.queryForObject(
                "select * from users where id = ?",
                userNotFollowers(), id);
        setFollowersIdsFromDateBase(user);
        log.info("Показ пользователя '{}'", id);
        return user;
    }

    public List<User> findAll() {
        List<User> users = template.query(
                "select * from users order by id asc",
                userNotFollowers());
        users.forEach(this::setFollowersIdsFromDateBase);
        return users;
    }

    public User addFollow(Integer userId, Integer friendId) {
        checkExistsById(userId);
        checkExistsById(friendId);
        template.update(
                "insert into follows (following_id, followed_id) values(?, ?)",
                friendId, userId);
        log.info("Подписка пользователя {} на {}", userId, friendId);
        return getById(userId);
    }

    public User removeFollowing(Integer userId, Integer friendId) {
        checkExistsById(userId);
        checkExistsById(friendId);
        template.update(
                "delete from follows where following_id = ? and followed_id = ?",
                friendId, userId);
        log.info("Удаление пользователя {} с {}", userId, friendId);
        return getById(userId);
    }

    public List<User> getSameFollowers(Integer userId, Integer friendId) {
        checkExistsById(userId);
        checkExistsById(friendId);
        List<User> sameFollowers = template.query(
                "select * from users as u " +
                        "join follows as f on f.following_id = u.id and f.followed_id = ? " +
                        "join follows as friend_f on friend_f.following_id = u.id and friend_f.followed_id = ?",
                userNotFollowers(), userId, friendId);
        sameFollowers.forEach(this::setFollowersIdsFromDateBase);
        log.info("Одинаковые подписчика {} и {}", userId, friendId);
        return sameFollowers;
    }

    public List<User> getFollowers(Integer userId) {
        checkExistsById(userId);
        List<User> followers = template.query(
                "select * from users as u " +
                        "join follows as f on f.following_id = u.id and f.followed_id = ?",
                userNotFollowers(), userId);
        followers.forEach(this::setFollowersIdsFromDateBase);
        log.info("Показ подписчиков {}", userId);
        return followers;
    }

    private RowMapper<User> userNotFollowers() {
        return (rs, rowNum) -> new User()
                .setId(rs.getInt("id"))
                .setName(rs.getString("name"))
                .setEmail(rs.getString("email"))
                .setLogin(rs.getString("login"))
                .setBirthday(rs.getDate("birthday").toLocalDate());
    }

    private void setFollowersIdsFromDateBase(User user) {
        user.getFriends().addAll(template.query(
                "select following_id from follows where followed_id = ?",
                (rs, rowNum) -> rs.getInt("following_id"), user.getId()));
    }

    private void checkExistsById(int id) {
        if (Boolean.FALSE.equals(template.queryForObject(
                "select exists (select id from users where id = ?) as match",
                (rs, rowNum) -> rs.getBoolean("match"), id))) {
            throw new NotFoundException("Нет пользователей с таким идентификатором: " + id);
        }
    }
}
