package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable Integer userId,
                          @PathVariable Integer friendId) {
        return userService.addFollow(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User removeFriend(@PathVariable Integer userId,
                             @PathVariable Integer friendId) {
        return userService.removeFollow(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable Integer id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> findSameFriends(@PathVariable Integer userId,
                                      @PathVariable Integer friendId) {
        return userService.getSameFriends(userId, friendId);
    }
}
