package ru.yandex.practicum.filmorate.service.rating;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.RatingDbStorage;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class InMemoryRatingService implements RatingService {
    private RatingDbStorage ratingDbStorage;

    @Override
    public Rating create(Rating rating) {
        return ratingDbStorage.create(rating);
    }

    @Override
    public Rating update(Rating rating) {
        return ratingDbStorage.update(rating);
    }

    @Override
    public Rating getById(int id) {
        return ratingDbStorage.getById(id);
    }

    @Override
    public List<Rating> getAll() {
        return ratingDbStorage.getAll();
    }

    @Override
    public void remove(int id) {
        ratingDbStorage.removeById(id);
    }
}
