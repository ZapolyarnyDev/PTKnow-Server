package io.github.zapolyarnydev.ptknow.exception.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String handle) {
        super(String.format("Пользователь с короткой ссылкой %s не найден", handle));
    }
}