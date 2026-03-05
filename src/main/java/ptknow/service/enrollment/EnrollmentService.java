package ptknow.service.enrollment;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ptknow.exception.course.CourseIsFullException;
import ptknow.exception.course.CourseNotFoundException;
import ptknow.exception.enrollment.AlreadyEnrolledException;
import ptknow.exception.enrollment.UserNotEnrollableException;
import ptknow.model.auth.Auth;
import ptknow.model.auth.Role;
import ptknow.model.course.Course;
import ptknow.model.enrollment.Enrollment;
import ptknow.repository.enrollment.EnrollmentRepository;
import ptknow.service.course.CourseService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EnrollmentService {

    EnrollmentRepository repository;
    CourseService courseService;

    @Transactional
    public Enrollment enroll(Auth initiator, Long courseId)
            throws UserNotEnrollableException, CourseNotFoundException,
            AlreadyEnrolledException, CourseIsFullException {
        if (!enrollable(initiator))
            throw new UserNotEnrollableException(initiator.getId());

        if(isEnrolled(initiator, courseId))
            throw new AlreadyEnrolledException(initiator.getId());

        var course = courseService.findCourseById(courseId);

        if(getEnrolledAmount(courseId) >= course.getMaxUsersAmount())
            throw new CourseIsFullException(courseId);

        var enrollment = Enrollment.builder()
                .user(initiator)
                .course(course)
                .enrollSince(Instant.now())
                .build();

        course.addEnrollment(enrollment);
        initiator.addEnrollment(enrollment);

        try {
            return repository.save(enrollment);
        } catch (DataIntegrityViolationException e) {
            if(isEnrolled(initiator, courseId))
                throw new AlreadyEnrolledException(initiator.getId());
           throw e;
        }
    }

    @Transactional(readOnly = true)
    public boolean isEnrolled(Auth user, Long courseId) {
        return repository.existsByUser_IdAndCourse_Id(user.getId(), courseId);
    }

    @Transactional(readOnly = true)
    public boolean isEnrolled(UUID userId, Long courseId) {
        return repository.existsByUser_IdAndCourse_Id(userId, courseId);
    }

    private boolean enrollable(Auth auth) {
        return auth.getRole() == Role.GUEST ||
                auth.getRole() == Role.STUDENT;
    }


    @Transactional(readOnly = true)
    public int getEnrolledAmount(Course course) {
        return getEnrolledAmount(course.getId());
    }

    @Transactional(readOnly = true)
    public int getEnrolledAmount(Long courseId) {
        return repository.countByCourse_Id(courseId);
    }

    @Transactional
    public void unenroll(Auth auth, Long courseId) {
        repository.deleteByUser_IdAndCourse_Id(auth.getId(), courseId);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> findAllByCourse(Long courseId) {
        return repository.findAllByCourse_Id(courseId);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> findAllByUser(Long userId) {
        return repository.findAllByUser_Id(userId);
    }
}
