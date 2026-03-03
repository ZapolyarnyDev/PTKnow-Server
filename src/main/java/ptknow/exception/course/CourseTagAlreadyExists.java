package ptknow.exception.course;

public class CourseTagAlreadyExists extends RuntimeException {
    public CourseTagAlreadyExists(String tag) {
        super(String.format("Course tag '%s' already exists", tag));
    }
}
