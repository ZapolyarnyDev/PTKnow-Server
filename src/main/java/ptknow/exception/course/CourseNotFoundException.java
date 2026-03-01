package ptknow.exception.course;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long courseId) {
        super(String.format("Курс с id '%d' не найден", courseId));
    }

    public CourseNotFoundException(String handle) {
        super(String.format("Курс с handle '%s' не найден", handle));
    }
}

