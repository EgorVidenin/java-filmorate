package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.RepeatException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InMemoryUserService implements UserService {
    private final UserStorage userStorage;

    @Override
    public User create(User user) {
        userStorage.create(user);
        return user;
    }

    @Override
    public User update(User user) {
        userStorage.update(user);
        return user;
    }

    @Override
    public User getById(int id) {
        return userStorage.getById(id);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User addFriends(int userId, int id) {
        User user = userStorage.getById(userId);
        User friends = userStorage.getById(id);
        if (user.getFriendsId().contains(id) || friends.getFriendsId().contains(userId)) {
            throw new RepeatException("Пользователи есть друг у друга в друзьях!");
        }
        user.addFriend(id);
        friends.addFriend(userId);
        userStorage.update(user);
        userStorage.update(friends);
        return friends;
    }

    @Override
    public User removeFriends(int userId, int id) {
        User user = userStorage.getById(userId);
        User friends = userStorage.getById(id);
        if (user.getFriendsId().isEmpty()) {
            throw new NoSuchElementException("Список друзей - пуст!");
        }
        if (!user.getFriendsId().contains(id) || !friends.getFriendsId().contains(userId)) {
            throw new RepeatException("Пользователи - не друзья!");
        }
        user.removeFriends(id);
        friends.removeFriends(userId);
        userStorage.update(user);
        userStorage.update(friends);
        return friends;
    }

    @Override
    public List<User> newFriends(int userId, int id) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(id);
        List<Integer> sameIds = user.getFriendsId().stream()
                .filter(friend.getFriendsId()::contains)
                .collect(Collectors.toList());
        return userStorage.getAll().stream()
                .filter(u -> sameIds.contains(u.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAllFriends(int userId) {
        User user = userStorage.getById(userId);
        return userStorage.getAll().stream()
                .filter(users -> user.getFriendsId().contains(users.getId()))
                .collect(Collectors.toList());
    }
}
