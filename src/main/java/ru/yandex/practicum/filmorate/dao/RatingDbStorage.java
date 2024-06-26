package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

@Component
@AllArgsConstructor
public class RatingDbStorage {
    private JdbcTemplate template;

    public Rating create(Rating rating) {
        template.update(
                "insert into ratings (name) values(?)",
                rating.getName());
        return template.queryForObject(
                "select * from ratings where name = ?",
                ratingRowMapper(), rating.getName());
    }

    public Rating update(Rating rating) {
        checkExistsById(rating.getId());
        template.update(
                "update ratings set name = ? where id = ?",
                rating.getName(),
                rating.getId());
        return rating;
    }

    public Rating getById(int ratingId) {
        checkExistsById(ratingId);
        return template.queryForObject(
                "select * from ratings where id = ?",
                ratingRowMapper(),
                ratingId);
    }

    public List<Rating> getAll() {
        return template.query("select * from ratings order by id", ratingRowMapper());
    }

    public void removeById(int ratingId) {
        checkExistsById(ratingId);
        template.update("delete from ratings where id = ?", ratingId);

    }

    private RowMapper<Rating> ratingRowMapper() {
        return (rs, rowNum) -> new Rating()
                .setId(rs.getInt("id"))
                .setName(rs.getString("name"));
    }

    private void checkExistsById(int id) {
        String select = "select exists (select id from ratings where id = ?) as match";
        if (Boolean.FALSE.equals(template.queryForObject(select,
                (rs, rowNum) -> rs.getBoolean("match"), id))) {
            throw new NotFoundException("Нет пользователей с таким идентификатором: " + id);
        }
    }
}
