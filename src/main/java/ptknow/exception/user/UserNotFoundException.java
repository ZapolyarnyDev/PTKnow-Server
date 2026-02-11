package ptknow.exception.user;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String handle) {
        super(String.format("Пользователь с короткой ссылкой %s не найден", handle));
    }

    public UserNotFoundException(UUID id) {
        super(String.format("Пользователь с ID %s не найден", id));
    }
}