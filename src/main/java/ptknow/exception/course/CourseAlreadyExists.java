package ptknow.exception.course;

public class CourseAlreadyExists extends RuntimeException {
    public CourseAlreadyExists(String course) {
        super(String.format("Course with name '%s' already exists", course));
    }
}
