package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Film create(@RequestBody Film film) {
        int id = generateId();
        film.setId(id);
        films.put(id, film);
        return film;
    }
    @PutMapping("/id")
    public Film update(@PathVariable int id, @RequestBody Film film) throws IllegalAccessException {
        if (films.containsKey(id)) {
            film.setId(id);
            films.put(id, film);
            return film;
        } else {
            throw new IllegalAccessException("Фильма с данным идентификатором:" + id + " - нет!");
        }
    }
    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }
}
