package ru.yandex.practicum.filmorate.service.genre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class InMemoryGenreService implements GenreService {

    private GenreDbStorage genreDbStorage;

    @Override
    public Genre create(Genre genre) {
        return genreDbStorage.create(genre);
    }

    @Override
    public Genre update(Genre genre) {
        return genreDbStorage.update(genre);
    }

    @Override
    public Genre getById(int id) {
        return genreDbStorage.getById(id);
    }

    @Override
    public List<Genre> getAll() {
        return genreDbStorage.getAll();
    }

    @Override
    public void remove(int id) {
        genreDbStorage.removeById(id);
    }
}

