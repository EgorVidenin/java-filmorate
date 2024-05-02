package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.yandex.practicum.filmorate.annotation.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Accessors(chain = true)
public class Film {
    private Integer id;
    @NotBlank
    @NotNull
    private String name;
    @Size(max = 200, message = "Описание содержит в себе > 200 символов!!!")
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;
    private Rating mpa;
    private Set<Integer> likes = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();
}
