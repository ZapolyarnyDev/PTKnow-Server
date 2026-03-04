package ptknow.exception.lesson;

import java.util.UUID;

public class LessonCannotBeCreatedException extends RuntimeException {
    public LessonCannotBeCreatedException(UUID uuid) {
        super("Lesson cannot be created by user with the id " + uuid);
    }
}