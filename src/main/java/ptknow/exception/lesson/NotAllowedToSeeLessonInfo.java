package ptknow.exception.lesson;

import java.util.UUID;

public class NotAllowedToSeeLessonInfo extends RuntimeException {
    public NotAllowedToSeeLessonInfo(UUID userId) {
        super("User with the id " + userId + " not allowed to see lesson info");
    }
}
