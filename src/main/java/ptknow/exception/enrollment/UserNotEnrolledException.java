package ptknow.exception.enrollment;

import java.util.UUID;

public class UserNotEnrolledException extends RuntimeException {
    public UserNotEnrolledException(UUID uuid) {
        super("User with the id " + uuid + " is not enrolled");
    }
}
