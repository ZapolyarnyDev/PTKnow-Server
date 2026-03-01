package ptknow.service.lesson;

import ptknow.dto.lesson.CreateLessonDTO;
import ptknow.model.course.Course;
import ptknow.model.lesson.Lesson;
import ptknow.exception.course.LessonNotFoundException;
import ptknow.repository.lesson.LessonRepository;
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
public class LessonService {

    LessonRepository lessonRepository;
    CourseService courseService;

    @Transactional
    public Lesson createLesson(Long courseId, CreateLessonDTO dto) {
        Course course = courseService.findCourseById(courseId);

        Lesson entity = Lesson.builder()
                .name(dto.name())
                .description(dto.description())
                .beginAt(dto.beginAt())
                .endsAt(dto.endsAt())
                .course(course)
                .lessonType(dto.type())
                .build();

        return lessonRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Lesson findById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Lesson> findAllByCourse(Long courseId) {
        return lessonRepository.getAllByCourse_Id(courseId);
    }

    @Transactional
    public void deleteById(Long id) {
        Lesson lesson = findById(id);
        lessonRepository.delete(lesson);
    }
}

