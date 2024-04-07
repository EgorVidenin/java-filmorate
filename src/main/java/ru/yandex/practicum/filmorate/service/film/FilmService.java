package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmService {
    Film create(Film film);

    Film update(Film film);

    Film getById(int id);

    List<Film> getAll();

    Film addLike(int filmId, int id);

    Film removeLike(int filmId, int id);

    List<Film> topFilm(int filmName);
}
