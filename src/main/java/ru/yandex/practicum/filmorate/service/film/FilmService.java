package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmService {
    Film create(Film film);

    Film update(Film film);

    Film getById(int id);

    List<Film> findAll();

    Film addLike(int filmId, int userId);

    Film removeLike(int filmId, int userId);

    List<Film> getTopPopularFilms(int count);
}
