package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserControllerTest {
    @Test
    public void testCreateUser() {
        UserController userController = new UserController();
        User user = new User("testUser", "test@example.com", LocalDate.of(2000, 1, 1));

        User createdUser = userController.create(user);

        assertNotNull(createdUser);
        assertEquals("testUser", createdUser.getName());
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals(LocalDate.of(2000, 1, 1), createdUser.getBirthday());
    }

    @Test
    public void testUpdateUser() {
        UserController userController = new UserController();
        User user = new User("testUser", "test@example.com", LocalDate.of(2000, 1, 1));
        userController.create(user);

        user.setEmail("updated@example.com");
        User updatedUser = userController.update(user);

        assertNotNull(updatedUser);
        assertEquals("updated@example.com", updatedUser.getEmail());
    }

    @Test
    public void testGetAllUsers() {
        UserController userController = new UserController();
        User user1 = new User("user1", "user1@example.com", LocalDate.of(1990, 5, 15));
        User user2 = new User("user2", "user2@example.com", LocalDate.of(1985, 10, 20));
        userController.create(user1);
        userController.create(user2);

        List<User> allUsers = userController.getAll();

        assertNotNull(allUsers);
        assertEquals(2, allUsers.size());
    }
}

