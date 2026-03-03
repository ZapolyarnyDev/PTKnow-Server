package ptknow.exception.course;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long courseId) {
        super(String.format("Course with id '%d' not found", courseId));
    }

    public CourseNotFoundException(String handle) {
        super(String.format("Course with handle '%s' not found", handle));
    }
}
