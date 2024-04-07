package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    private int generateId() {
        return nextId++;
    }

    @Override
    public Film create(Film film) {
        if (films.values().stream().anyMatch(existingFilm -> Objects.equals(existingFilm.getName(), film.getName()))) {
            log.info("Фильм {} уже существует", film.getName());
            throw new IllegalArgumentException("Фильм с таким названием уже существует");
        }
        int id = generateId();
        film.setId(id);
        films.put(film.getId(), film);
        log.info("Фильм {} успешно добавлен", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("Фильм {} не обновлён", film.getName());
            throw new NotFoundException("Такого Id нет!");

        }
        films.put(film.getId(), film);
        log.info("Фильм {} успешно обновлён", film.getName());
        return film;
    }

    @Override
    public Film getById(int id) {
        if (!films.containsKey(id)) {
            log.info("Некорректный ID");
            throw new NotFoundException("Фильм не найден!");
        }
        return films.get(id);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }
}
