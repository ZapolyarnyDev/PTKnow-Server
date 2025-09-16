package io.github.zapolyarnydev.ptknow.exception.email;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String email) {
        super(String.format("Пользователь с почтой %s не найден", email));
    }
}
