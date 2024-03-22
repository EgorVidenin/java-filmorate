package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.ReleaseDate;

import javax.validation.constraints.*;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private int id;
    @NotBlank
    @NotNull
    private String name;
    @Size(max = 200, message = "Описание содержит в себе > 200 символов!!!")
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
