package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public User create(User user) {
        int id = generateId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @PutMapping
    public User update(@PathVariable int id, @RequestBody User user) throws IllegalAccessException {
        if (users.containsKey(id)) {
            user.setId(id);
            users.put(id, user);
            return user;
        } else {
            throw new IllegalAccessException("Пользователя с данным идентификатором:" + id + " - нет!");
        }
    }

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
