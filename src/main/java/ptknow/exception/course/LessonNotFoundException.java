package ptknow.exception.course;

public class LessonNotFoundException extends RuntimeException {
    public LessonNotFoundException(Long id) {
        super(String.format("Занятие с id %d не найдено", id));
    }
}
