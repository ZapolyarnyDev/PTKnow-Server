package ptknow.exception.user;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String handle) {
        super(String.format("User with handle %s not found", handle));
    }

    public UserNotFoundException(UUID id) {
        super(String.format("User with id %s not found", id));
    }
}
