package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class User {
    private int id;
    @NotBlank
    @NotNull
    @Email
    private String email;
    @NotBlank
    @NotNull
    private String login;
    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9]*$", message = "Имя должно содержать хотя бы один символ!")
    private String name;
    @Past
    private String birthday;

    public User(String email, String login, String name, String birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
