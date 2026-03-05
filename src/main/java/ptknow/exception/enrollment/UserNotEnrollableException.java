package ptknow.exception.enrollment;

import java.util.UUID;

public class UserNotEnrollableException extends RuntimeException {
    public UserNotEnrollableException(UUID uuid) {
        super("User with the id " + uuid + " is not enrollable");
    }
}
