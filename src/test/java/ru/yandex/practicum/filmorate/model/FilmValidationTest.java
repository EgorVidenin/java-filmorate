package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

class FilmValidationTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldNotPassValidation() {
        Film filmWithWrongDate = new Film(
                "Film 1", "Description 1", LocalDate.now(), 120);
        Set<ConstraintViolation<Film>> violationsRelease = validator.validate(filmWithWrongDate);
        assertFalse(violationsRelease.isEmpty());
        //название не может быть пустым;
        Film filmWithWrongName = new Film(
                "Film 1", "Description 1", LocalDate.now(), 120);
        Set<ConstraintViolation<Film>> violationsName = validator.validate(filmWithWrongName);
        assertFalse(violationsName.isEmpty());
        //продолжительность фильма должна быть положительной.
        Film filmWithWrongDuration = new Film(
                "Film 1", "Description 1", LocalDate.now(), -120);
        Set<ConstraintViolation<Film>> violationsDuration = validator.validate(filmWithWrongDuration);
        assertFalse(violationsDuration.isEmpty());
        //максимальная длина описания — 200 символов;
        String description = "i".repeat(201);
        Film filmWithWrongDescription = new Film("Film 1", "Description 1", LocalDate.now(), 120);
        Set<ConstraintViolation<Film>> violationsDescription = validator.validate(filmWithWrongDescription);
        assertFalse(violationsDescription.isEmpty());
    }
}