package io.github.zapolyarnydev.ptknow.service.course;

import io.github.zapolyarnydev.ptknow.dto.course.CreateCourseDTO;
import io.github.zapolyarnydev.ptknow.entity.course.CourseEntity;
import io.github.zapolyarnydev.ptknow.entity.course.CourseTagEntity;
import io.github.zapolyarnydev.ptknow.exception.course.CourseAlreadyExists;
import io.github.zapolyarnydev.ptknow.exception.course.CourseNotFoundException;
import io.github.zapolyarnydev.ptknow.exception.course.CourseTagAlreadyExists;
import io.github.zapolyarnydev.ptknow.generator.handle.HandleGenerator;
import io.github.zapolyarnydev.ptknow.repository.course.CourseRepository;
import io.github.zapolyarnydev.ptknow.repository.course.CourseTagRepository;
import io.github.zapolyarnydev.ptknow.service.HandleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseService implements HandleService<CourseEntity> {
    CourseRepository courseRepository;
    CourseTagRepository courseTagRepository;
    HandleGenerator handleGenerator;

    @Transactional
    public CourseEntity publishCourse(CreateCourseDTO dto) {
        if(courseRepository.existsByName(dto.name()))
            throw new CourseAlreadyExists(dto.name());

        String handle = handleGenerator.generate(courseRepository::existsByHandle);

        var entity = CourseEntity.builder()
                .name(dto.name())
                .description(dto.description())
                .courseTags(courseTagsFromNames(dto.tags()))
                .handle(handle)
                .build();

        courseRepository.save(entity);

        return entity;
    }

    @Transactional
    public List<CourseTagEntity> courseTagsFromNames(List<String> names) {
        return names.stream()
                .map(name -> courseTagRepository.findByName(name)
                        .orElseGet(() -> createCourseTag(name)))
                .toList();
    }

    @Transactional
    public CourseTagEntity createCourseTag(String name) {
        if(courseTagRepository.existsByName(name))
            throw new CourseTagAlreadyExists(name);

        var entity = new CourseTagEntity(name);
        courseTagRepository.save(entity);

        return entity;
    }

    @Transactional
    public void deleteCourseById(Long courseId) {
        var course = findCourseById(courseId);

        List<CourseTagEntity> tags = course.getCourseTags();
        courseRepository.delete(course);

        for (CourseTagEntity tag : tags) {
            if (courseRepository.countByCourseTagsContains(tag) == 0) {
                courseTagRepository.delete(tag);
            }
        }
    }

    @Transactional(readOnly = true)
    public CourseEntity findCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    @Transactional(readOnly = true)
    public List<CourseEntity> findAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public CourseEntity getByHandle(String handle) {
        return courseRepository.findByHandle(handle)
                .orElseThrow(() -> new CourseNotFoundException(handle));
    }
}
