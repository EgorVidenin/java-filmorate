package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.RepeatException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.time.ZoneId;
import java.util.*;

@Component
@AllArgsConstructor
@Slf4j
public class FilmDbStorage {
    private JdbcTemplate template;

    public Film create(Film film) {
        template.update(
                "insert into films (name, description, release, duration) values(?, ?, ?, ?)",
                film.getName(),
                film.getDescription(),
                Date.from(film.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                film.getDuration());
        film.setId(template.queryForObject(
                "select max(id) as max from films",
                (rs, rowNum) -> rs.getInt("max")));
        log.info("Сохранённые фильмы без рейтинга и жанров");
        if (film.getMpa() != null) {
            Integer ratingId = film.getMpa().getId();
            validateIdExistence(ratingId, "ratings");
            String ratingName = template.queryForObject(
                    "select name from ratings where id = ?",
                    (rs, rowNum) -> rs.getString("name"), ratingId);
            film.getMpa().setName(ratingName);
            template.update(
                    "update films set rating_id = ? where id = ?",
                    ratingId, film.getId());
            log.info("Сохранённый рейтинг {} фильма", ratingName);
        }
        Set<Genre> genres = film.getGenres();
        if (!genres.isEmpty()) {
            film.getGenres().forEach(genre -> validateIdExistence(genre.getId(), "genres"));
            film.getGenres().forEach(genre -> genre.setName(template.queryForObject(
                    "select name from genres where id = ?",
                    (rs, rowNum) -> rs.getString("name"), genre.getId())));
            film.getGenres().forEach(genre -> template.update(
                    "insert into genres_films (genre_id, film_id) values (?, ?)",
                    genre.getId(), film.getId()));
            log.info("Сохранённые жанры фильма");
        }
        return film;
    }

    public Film update(Film film) {
        checkSomething(film.getId(), "films");
        template.update(
                "update films set name = ?, description = ?, release = ?, duration = ? where id = ?",
                film.getName(),
                film.getDescription(),
                Date.from(film.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                film.getDuration(),
                film.getId());
        log.info("Обновление информации о фильме");
        if (film.getMpa() != null) {
            checkSomething(film.getMpa().getId(), "ratings");
            template.update(
                    "update films set rating_id = ? where id = ?",
                    film.getMpa().getId(), film.getId());
            String ratingName = template.queryForObject(
                    "select name FROM ratings where id = ?",
                    (rs, rowNum) -> rs.getString("name"), film.getMpa().getId());
            film.getMpa().setName(ratingName);
            log.info("Обновление плёнки в таблице");
        }
        Set<Genre> genres = film.getGenres();
        if (!genres.isEmpty()) {
            film.getGenres().forEach(genre -> validateIdExistence(genre.getId(), "genres"));
            template.update(
                    "delete from genres_films where film_id = ?",
                    film.getId());
            film.getGenres().forEach(genre -> template.update(
                    "insert into genres_films (genre_id, film_id) values (?, ?)",
                    genre.getId(), film.getId()));
            film.getGenres().forEach(genre -> genre.setName(template.queryForObject(
                    "select name from genres where id = ?",
                    (rs, rowNum) -> rs.getString("name"), genre.getId())));
            log.info("Обновление жанра фильма");
        }
        return film;
    }

    public Film getById(Integer id) {
        checkSomething(id, "films");
        Film film = template.queryForObject(
                "select name, description, release, duration from films where id = ?",
                (rs, rowNum) -> new Film()
                        .setId(id)
                        .setName(rs.getString("name"))
                        .setDescription(rs.getString("description"))
                        .setDuration(rs.getInt("duration"))
                        .setReleaseDate(rs.getDate("release").toLocalDate()), id);

        if (isSetFilmRating(id)) {
            Integer ratingId = template.queryForObject(
                    "select rating_id as mpa_id from films where id = ?",
                    (rs, rowNum) -> rs.getInt("mpa_id"), id);
            Rating mpa = template.queryForObject(
                    "select * from ratings where id = ?",
                    (rs, rowNum) -> new Rating()
                            .setName(rs.getString("name"))
                            .setId(rs.getInt("id")), ratingId);
            film.setMpa(mpa);
        }
        List<Integer> genresIds = template.query(
                "select genre_id as id from genres_films where film_id = ?",
                (rs, rowNum) -> rs.getInt("id"), id);
        if (!genresIds.isEmpty()) {
            List<Genre> genresList = new LinkedList<>();
            genresIds.forEach(genreId -> genresList.add(template.queryForObject(
                    "select * from genres where id = ?",
                    (rs, rowNum) -> new Genre()
                            .setId(rs.getInt("id"))
                            .setName(rs.getString("name")), genreId)));
            film.getGenres().addAll(genresList);
        }
        List<Integer> likesList = template.query(
                "select user_id as id from likes where film_id = ?",
                (rs, rowNum) -> rs.getInt("id"), id);
        if (!likesList.isEmpty()) {
            film.getLikes().addAll(likesList);
        }
        log.info("Показ фильма {}", id);
        return film;
    }

    public List<Film> findAll() {
        List<Integer> filmsId = template.query(
                "select id from films order by id asc",
                (rs, rowNum) -> rs.getInt("id"));
        List<Film> films = new ArrayList<>();
        filmsId.forEach(id -> films.add(getById(id)));
        return films;
    }

    public Film addLike(Integer filmId, Integer userId) {
        checkSomething(filmId, "films");
        checkSomething(userId, "users");
        if (isFilmLikedByUser(filmId, userId)) {
            throw new RepeatException("Фильм может понравиться пользователю один раз");
        }
        template.update("insert into likes (film_id, user_id) values(?, ?)", filmId, userId);
        log.info("Добавить {} пользователья, которому нравится фильм {}", userId, filmId);
        return getById(filmId);
    }

    public Film removeLike(Integer filmId, Integer userId) {
        checkSomething(filmId, "films");
        checkSomething(userId, "users");
        if (!isFilmLikedByUser(filmId, userId)) {
            throw new RepeatException("No like by user with ID: " + userId);
        }
        template.update("delete from likes where film_id = ? and user_id = ?", filmId, userId);
        log.info("Удалить лайк {} из фильма {}", userId, filmId);
        return getById(filmId);
    }

    public List<Film> getTopPopularFilms(int count) {
        List<Integer> topIds = template.query(
                "select film_id as id from likes group by film_id order by count(user_id) desc limit(?)",
                (rs, rowNum) -> rs.getInt("id"), count);
        List<Film> topFilms = new ArrayList<>();
        topIds.forEach(id -> topFilms.add(getById(id)));
        log.info("Показ лучшего {} фильма", count);
        return topFilms;
    }

    private boolean isFilmLikedByUser(int filmId, int userId) {
        return Boolean.TRUE.equals(template.queryForObject(
                "select exists (select * from likes where film_id = ? and user_id = ?) as match",
                (rs, rowNum) -> rs.getBoolean("match"), filmId, userId));
    }

    private boolean isSetFilmRating(Integer id) {
        return Boolean.TRUE.equals(template.queryForObject(
                "select exists (select rating_id from films where rating_id is not null and id = ?) as match",
                (rs, rowNum) -> rs.getBoolean("match"), id));
    }

    private void checkSomething(int id, String tableName) {
        String select = "select exists (select id as match from " + tableName + " where id = ?) as match";
        if (Boolean.FALSE.equals(template.queryForObject(select,
                (rs, rowNum) -> rs.getBoolean("match"), id))) {
            throw new NotFoundException("Нет " + tableName + " такого ID: " + id);
        }
    }

    private void validateIdExistence(int id, String tableName) {
        String select = "select exists (select id as match from " + tableName + " where id = ?) as match";
        if (Boolean.FALSE.equals(template.queryForObject(select,
                (rs, rowNum) -> rs.getBoolean("match"), id))) {
            throw new ValidationException("Нет " + tableName + " такого ID: " + id);
        }
    }
}