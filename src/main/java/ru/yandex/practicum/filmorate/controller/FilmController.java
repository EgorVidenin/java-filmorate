package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    private int generateId() {
        return nextId++;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
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

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("Фильм {} не обновлён", film.getName());
            throw new IllegalArgumentException();
        }
            films.put(film.getId(), film);
            log.info("Фильм {} успешно обновлён", film.getName());
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }
}