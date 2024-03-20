package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    public void setup() {
        filmController = new FilmController();
    }
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    @Test
    public void testCreateFilm() {
        Film film = new Film("Film 1", "Description 1", LocalDate.now(), 120);
        film.setName("Славные парни");
        Film createdFilm = filmController.create(film);
        assertEquals("Славные парни", createdFilm.getName());
    }

    @Test
    public void testUpdateFilm() {
        Film film = new Film("Film 1", "Description 1", LocalDate.now(), 90);
        film.setName("Форсаж");
        Film createdFilm = filmController.create(film);

        createdFilm.setName("Форсаж");
        Film updatedFilm = filmController.update(createdFilm);

        assertEquals("Форсаж", updatedFilm.getName());
    }

    @Test
    public void testGetAllFilms() {
        Film film1 = new Film("Film 1", "Description 1", LocalDate.now(), 120);
        film1.setName("Film 1");
        Film film2 = new Film("Film 1", "Description 1", LocalDate.now(), 90);
        film2.setName("Film 2");

        filmController.create(film1);
        filmController.create(film2);

        List<Film> films = filmController.getAll();
        assertEquals(2, films.size());
    }

    @Test
    public void testCreateDuplicateFilm() {
        Film film1 = new Film("Film 1", "Description 1", LocalDate.now(), 200);
        filmController.create(film1);

        Film duplicateFilm = new Film("Film 1", "Description 1", LocalDate.now(), 200);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> filmController.create(duplicateFilm));
        assertEquals("Фильм с таким названием уже существует", exception.getMessage());
    }

    @Test
    public void testUpdateNonExistingFilm() {
        Film film = new Film("Film 1", "Description 1", LocalDate.now(), 120);
        film.setName("Джокер");

        assertThrows(IllegalArgumentException.class, () -> filmController.update(film));
    }
}