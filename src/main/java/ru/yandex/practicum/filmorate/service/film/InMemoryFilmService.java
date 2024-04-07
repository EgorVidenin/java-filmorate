package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.RepeatException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Override
    public Film create(Film film) {
        filmStorage.create(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        filmStorage.update(film);
        return film;
    }

    @Override
    public Film getById(int id) {
        return filmStorage.getById(id);
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film addLike(int filmId, int id) {
        userService.getById(id);
        Film film = filmStorage.getById(filmId);
        if (film.getLikes().contains(id)) {
            throw new RepeatException("Лайк уже стоит!");
        }
        film.addLike(id);
        filmStorage.update(film);
        return film;
    }

    @Override
    public Film removeLike(int filmId, int id) {
        userService.getById(id);
        Film film = filmStorage.getById(filmId);
        if (!film.getLikes().contains(id)) {
            throw new RepeatException("Пользователь не поставил лайк!");
        }
        film.removeLike(id);
        filmStorage.update(film);
        return film;
    }

    @Override
    public List<Film> topFilm(int filmName) {
        return filmStorage.getAll().stream()
                .filter(film -> !film.getLikes().isEmpty())
                .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
                .limit(filmName)
                .collect(Collectors.toList());
    }
}
