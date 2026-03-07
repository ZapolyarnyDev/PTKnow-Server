package ptknow.exception.course;

import java.util.UUID;

public class NotAllowedToSeeCourseInfoException extends RuntimeException {
    public NotAllowedToSeeCourseInfoException(UUID userId) {
        super("User with the id " + userId + " not allowed to see course info");
    }
}
