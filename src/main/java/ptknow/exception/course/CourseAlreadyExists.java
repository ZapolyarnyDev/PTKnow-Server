package ptknow.exception.course;

public class CourseAlreadyExists extends RuntimeException {
    public CourseAlreadyExists(String course) {
        super(String.format("Курс с названием '%s' уже используется", course));
    }
}

