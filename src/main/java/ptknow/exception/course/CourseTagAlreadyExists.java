package ptknow.exception.course;

public class CourseTagAlreadyExists extends RuntimeException {
    public CourseTagAlreadyExists(String tag) {
        super(String.format("Тег '%s' для курсов уже существует", tag));
    }
}

