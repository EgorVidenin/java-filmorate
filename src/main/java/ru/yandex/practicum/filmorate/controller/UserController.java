package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    private int generateId() {
        return nextId++;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            log.info("Клиент {} уже существует", user.getName());
            throw new IllegalArgumentException();
        }
            int id = generateId();
            user.setId(id);
            users.put(user.getId(), user);
            log.info("Клиент {} успешно добавден", user.getName());
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Клиент {} не обновлён", user.getName());
            throw new IllegalArgumentException();
        }
            users.put(user.getId(), user);
            log.info("Клиент {} успешно обновлён", user.getName());
        return user;
    }

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
