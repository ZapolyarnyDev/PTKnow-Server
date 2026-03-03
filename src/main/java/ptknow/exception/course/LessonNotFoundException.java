package ptknow.exception.course;

public class LessonNotFoundException extends RuntimeException {
    public LessonNotFoundException(Long id) {
        super(String.format("Lesson with id %d not found", id));
    }
}
