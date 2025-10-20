package io.github.zapolyarnydev.ptknow.service.course;

import io.github.zapolyarnydev.ptknow.dto.course.CreateLessonDTO;
import io.github.zapolyarnydev.ptknow.entity.course.CourseEntity;
import io.github.zapolyarnydev.ptknow.entity.lesson.LessonEntity;
import io.github.zapolyarnydev.ptknow.exception.course.LessonNotFoundException;
import io.github.zapolyarnydev.ptknow.repository.course.LessonRepository;
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
    public LessonEntity createLesson(Long courseId, CreateLessonDTO dto) {
        CourseEntity course = courseService.findCourseById(courseId);

        LessonEntity entity = LessonEntity.builder()
                .name(dto.name())
                .description(dto.description())
                .beginAt(dto.beginAt())
                .endsAt(dto.endsAt())
                .course(course)
                .build();

        return lessonRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public LessonEntity findById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<LessonEntity> findAllByCourse(Long courseId) {
        return lessonRepository.getAllByCourse_Id(courseId);
    }

    @Transactional
    public void deleteById(Long id) {
        LessonEntity lesson = findById(id);
        lessonRepository.delete(lesson);
    }
}
