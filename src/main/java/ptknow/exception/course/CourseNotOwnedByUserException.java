package ptknow.exception.course;

import java.util.UUID;

public class CourseNotOwnedByUserException extends RuntimeException {
    public CourseNotOwnedByUserException(UUID id) {
      super(String.format("Course not owned by user with the id '%s'", id));
    }
}
