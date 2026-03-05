package ptknow.exception.enrollment;

import java.util.UUID;

public class AlreadyEnrolledException extends RuntimeException {
    public AlreadyEnrolledException(UUID uuid) {
        super("User with the id " + uuid + " already enrolled");
    }
}
