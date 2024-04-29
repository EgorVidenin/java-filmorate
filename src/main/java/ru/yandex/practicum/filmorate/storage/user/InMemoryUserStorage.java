package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.RepeatException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    private int generateId() {
        return nextId++;
    }

    @Override
    public User create(User user) {
        if (users.containsKey(user.getId())) {
            log.info("Клиент {} уже существует", user.getName());
            throw new RepeatException("User already exist");
        }
        int id = generateId();
        user.setId(id);
        users.put(user.getId(), user);
        log.info("Клиент {} успешно добавден", user.getName());
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Клиент {} не обновлён", user.getName());
            throw new NotFoundException("Такого ID нет");
        }
        users.put(user.getId(), user);
        log.info("Клиент {} успешно обновлён", user.getName());
        return user;
    }

    @Override
    public User getById(int id) {
        if (!users.containsKey(id)) {
            log.info("Не корректный ID");
            throw new NotFoundException("Пользователь не найден!");
        }
        return users.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
