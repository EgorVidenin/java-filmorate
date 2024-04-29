package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {

    private FilmDbStorage filmDbStorage;

    @Override
    public Film create(Film film) {
        filmDbStorage.create(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        filmDbStorage.update(film);
        return film;
    }

    @Override
    public Film getById(int id) {
        return filmDbStorage.getById(id);
    }

    @Override
    public List<Film> findAll() {
        return filmDbStorage.findAll();
    }

    @Override
    public Film addLike(int filmId, int userId) {
        return filmDbStorage.addLike(filmId, userId);
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        return filmDbStorage.removeLike(filmId, userId);
    }

    @Override
    public List<Film> getTopPopularFilms(int count) {
        return filmDbStorage.getTopPopularFilms(count);
    }
}