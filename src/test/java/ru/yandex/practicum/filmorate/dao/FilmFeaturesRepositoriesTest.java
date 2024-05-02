package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import static org.assertj.core.api.Assertions.assertThat;
    @JdbcTest
    @RequiredArgsConstructor(onConstructor_ = @Autowired)
    class FilmFeaturesRepositoriesTest {

        private final JdbcTemplate template;

        @Test
        void testGetGenreById() {
            GenreDbStorage repository = new GenreDbStorage(template);
            Genre genre = new Genre().setId(7).setName("Экшн");
            repository.create(genre);
            Genre savedGenre = repository.getById(7);
            assertThat(savedGenre)
                    .isNotNull()
                    .usingRecursiveComparison().isEqualTo(genre);
        }

        @Test
        void getRatingById() {
            RatingDbStorage repository = new RatingDbStorage(template);
            Rating rating = new Rating().setId(6).setName("XYZ");
            repository.create(rating);
            Rating savedRating = repository.getById(6);
            assertThat(savedRating)
                    .isNotNull()
                    .usingRecursiveComparison().isEqualTo(rating);
        }
    }
