package ptknow.exception.course;

import java.util.UUID;

public class CourseCannotBeEditByUserException extends RuntimeException {
    public CourseCannotBeEditByUserException(UUID id) {
        super(String.format("Course cannot be edit by user with the id '%s'", id));
    }
}
