package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;
    @NotNull
    @Email
    private String email;
    @NotBlank
    @NotNull
    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9]*$", message = "Имя должно содержать хотя бы один символ!")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    private Set<Integer> friendsId = new HashSet<>();

    public void addFriend(int id) {
        friendsId.add(id);
    }

    public void removeFriends(int id) {
        friendsId.remove(id);
    }

    public User(String login, String email, LocalDate birthday) {
        this.name = login;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }
}