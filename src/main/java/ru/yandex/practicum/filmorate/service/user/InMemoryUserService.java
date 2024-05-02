package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@AllArgsConstructor
public class InMemoryUserService implements UserService {
    private UserDbStorage userDbStorage;

    @Override
    public User create(User user) {
        return userDbStorage.create(user);
    }

    @Override
    public User update(User user) {
        return userDbStorage.update(user);
    }

    @Override
    public User getById(int id) {
        return userDbStorage.getById(id);
    }

    @Override
    public List<User> findAll() {
        return userDbStorage.findAll();
    }

    @Override
    public User addFollow(int followingId, int followedId) {
        return userDbStorage.addFollow(followingId, followedId);
    }

    @Override
    public User removeFollow(int followingId, int followedId) {
        return userDbStorage.removeFollowing(followingId, followedId);
    }

    @Override
    public List<User> getSameFriends(int userId, int friendId) {
        return userDbStorage.getSameFollowers(userId, friendId);
    }

    @Override
    public List<User> getFriends(int userId) {
        return userDbStorage.getFollowers(userId);
    }

}
