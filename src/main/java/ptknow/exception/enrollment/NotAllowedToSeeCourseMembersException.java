package ptknow.exception.enrollment;

import java.util.UUID;

public class NotAllowedToSeeCourseMembersException extends RuntimeException {
    public NotAllowedToSeeCourseMembersException(UUID uuid) {
        super("User with the id " + uuid + " not allowed to see course members");
    }
}
