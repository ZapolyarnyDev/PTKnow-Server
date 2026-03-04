package ptknow.exception.lesson;

import java.util.UUID;

public class LessonNotOwnedException extends RuntimeException {
    public LessonNotOwnedException(UUID uuid) {
        super("Lesson not owned by user with the id " + uuid);
    }
}
