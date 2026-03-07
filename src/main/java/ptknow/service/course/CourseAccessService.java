package ptknow.service.course;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ptknow.exception.course.CourseNotFoundException;
import ptknow.exception.course.NotAllowedToSeeCourseInfoException;
import ptknow.model.auth.Auth;
import ptknow.model.auth.Role;
import ptknow.model.course.Course;
import ptknow.repository.course.CourseRepository;
import ptknow.repository.enrollment.EnrollmentRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseAccessService {

    CourseRepository courseRepository;
    EnrollmentRepository enrollmentRepository;

    @Transactional(readOnly = true)
    public Course access(Long courseId, Auth auth) {
        var course = findCourseById(courseId);
        return access(course, auth);
    }


    @Transactional(readOnly = true)
    public Course access(String courseHandle, Auth auth) {
        var course = findCourseByHandle(courseHandle);
        return access(course, auth);
    }

    @Transactional(readOnly = true)
    public Course access(Course course, Auth auth) {
        boolean canSee = auth.getRole() == Role.ADMIN ||
                course.getOwner().equals(auth) ||
                course.hasEditor(auth) ||
                enrollmentRepository.existsByUser_IdAndCourse_Id(auth.getId(), course.getId());

        if(canSee)
            return course;
        throw new NotAllowedToSeeCourseInfoException(auth.getId());
    }

    private Course findCourseByHandle(String handle) {
        return courseRepository.findByHandle(handle)
                .orElseThrow(() -> new CourseNotFoundException(handle));
    }

    private Course findCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }
}
