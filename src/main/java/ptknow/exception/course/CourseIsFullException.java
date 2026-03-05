package ptknow.exception.course;

public class CourseIsFullException extends RuntimeException{

    public CourseIsFullException(Long courseId) {
        super(String.format("Course with id %d is full.", courseId));
    }
}
