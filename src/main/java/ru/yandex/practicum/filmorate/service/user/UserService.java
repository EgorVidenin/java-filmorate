package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserService {
    User create(User user);

    User update(User user);

    User getById(int id);

    List<User> getAll();

    User addFriends(int userId, int id);

    User removeFriends(int userId, int id);

    List<User> newFriends(int userId, int id);

    List<User> getAllFriends(int userId);

}
