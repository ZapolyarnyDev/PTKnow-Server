package ptknow.service.lesson;

import ptknow.dto.lesson.CreateLessonDTO;
import ptknow.exception.lesson.LessonCannotBeCreatedException;
import ptknow.exception.lesson.LessonNotOwnedException;
import ptknow.exception.lesson.NotAllowedToSeeLessonInfo;
import ptknow.model.auth.Auth;
import ptknow.model.auth.Role;
import ptknow.model.course.Course;
import ptknow.model.lesson.Lesson;
import ptknow.exception.lesson.LessonNotFoundException;
import ptknow.repository.lesson.LessonRepository;
import ptknow.service.AccessService;
import ptknow.service.OwnershipService;
import ptknow.service.course.CourseAccessService;
import ptknow.service.course.CourseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonService implements OwnershipService<Long>, AccessService<Long> {

    LessonRepository lessonRepository;
    CourseService courseService;
    CourseAccessService accessService;

    @Transactional
    public Lesson createLesson(Long courseId, Auth initiator, CreateLessonDTO dto) throws LessonCannotBeCreatedException {
        Course course = courseService.findCourseById(courseId);

        if (!courseService.canEdit(course, initiator))
            throw new LessonCannotBeCreatedException(initiator.getId());

        Lesson entity = Lesson.builder()
                .name(dto.name())
                .description(dto.description())
                .beginAt(dto.beginAt())
                .endsAt(dto.endsAt())
                .course(course)
                .lessonType(dto.type())
                .owner(initiator)
                .build();

        initiator.addOwnedLesson(entity);

        return lessonRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Lesson findById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Lesson seeById(Long id, Auth initiator) throws NotAllowedToSeeLessonInfo {
        var lesson = findById(id);

        if(!accessService.canSee(lesson.getCourse(), initiator))
            throw new NotAllowedToSeeLessonInfo(initiator.getId());

        return lesson;
    }

    @Transactional(readOnly = true)
    public List<Lesson> findAllByCourse(Long courseId) {
        return lessonRepository.getAllByCourse_Id(courseId);
    }

    @Transactional(readOnly = true)
    public List<Lesson> findAllByCourse(Long courseId, Auth initiator) {
        if(!accessService.canSee(courseId, initiator))
            throw new NotAllowedToSeeLessonInfo(initiator.getId());

        return lessonRepository.getAllByCourse_Id(courseId);
    }

    @Transactional
    public void deleteById(Long id, Auth initiator) throws LessonNotOwnedException {
        Lesson lesson = findById(id);

        if (!canDelete(lesson, initiator))
            throw new LessonNotOwnedException(initiator.getId());

        lessonRepository.delete(lesson);
    }

    @Override
    public boolean isOwner(Long resourceId, Auth auth) {
        return lessonRepository.existsByIdAndOwner_Id(resourceId, auth.getId());
    }

    @Override
    public Auth getOwner(Long resourceId) {
        return findById(resourceId).getOwner();
    }

    public boolean canEdit(Lesson lesson, Auth auth) {
        return auth.getRole() == Role.ADMIN ||
                lesson.getOwner().equals(auth) ||
                courseService.canEdit(lesson.getCourse(), auth);
    }

    public boolean canDelete(Lesson lesson, Auth auth) {
        return auth.getRole() == Role.ADMIN ||
                lesson.getOwner().equals(auth) ||
                lesson.getCourse().getOwner().equals(auth);
    }

    @Override
    public boolean canSee(Long id, Auth initiator) {
        var lesson = findById(id);
        return accessService.canSee(lesson.getCourse().getId(), initiator);
    }
}

