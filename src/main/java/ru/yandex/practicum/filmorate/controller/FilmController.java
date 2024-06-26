package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private FilmService filmService;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("{id}")
    public Film getById(@PathVariable Integer id) {
        return filmService.getById(id);
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLike(@PathVariable int filmId,
                        @PathVariable int userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film removeLike(@PathVariable int filmId,
                           @PathVariable int userId) {
        return filmService.removeLike(filmId, userId);
    }

    @GetMapping("popular")
    public List<Film> findTopPopularFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        return filmService.getTopPopularFilms(count);
    }
}