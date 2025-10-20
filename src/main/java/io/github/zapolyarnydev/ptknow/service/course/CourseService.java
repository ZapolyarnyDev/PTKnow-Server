package io.github.zapolyarnydev.ptknow.service.course;

import io.github.zapolyarnydev.ptknow.dto.course.CreateCourseDTO;
import io.github.zapolyarnydev.ptknow.entity.course.CourseEntity;
import io.github.zapolyarnydev.ptknow.entity.course.CourseTagEntity;
import io.github.zapolyarnydev.ptknow.entity.file.FileEntity;
import io.github.zapolyarnydev.ptknow.exception.course.CourseAlreadyExists;
import io.github.zapolyarnydev.ptknow.exception.course.CourseNotFoundException;
import io.github.zapolyarnydev.ptknow.exception.course.CourseTagAlreadyExists;
import io.github.zapolyarnydev.ptknow.generator.handle.HandleGenerator;
import io.github.zapolyarnydev.ptknow.repository.course.CourseRepository;
import io.github.zapolyarnydev.ptknow.repository.course.CourseTagRepository;
import io.github.zapolyarnydev.ptknow.service.HandleService;
import io.github.zapolyarnydev.ptknow.service.file.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseService implements HandleService<CourseEntity> {

    CourseRepository repository;
    CourseTagRepository courseTagRepository;
    HandleGenerator handleGenerator;
    FileService fileService;

    @Transactional
    public CourseEntity publishCourse(CreateCourseDTO dto, MultipartFile preview) throws IOException {
        if (repository.existsByName(dto.name())) {
            throw new CourseAlreadyExists(dto.name());
        }

        String handle = handleGenerator.generate(repository::existsByHandle);

        FileEntity previewFile = null;
        if (preview != null && !preview.isEmpty()) {
            previewFile = fileService.saveFile(preview);
        }

        var entity = CourseEntity.builder()
                .name(dto.name())
                .description(dto.description())
                .courseTags(courseTagsFromNames(dto.tags()))
                .handle(handle)
                .preview(previewFile)
                .build();

        repository.save(entity);
        return entity;
    }


    @Transactional
    public Set<CourseTagEntity> courseTagsFromNames(Set<String> names) {
        return names.stream()
                .map(name -> courseTagRepository.findByName(name)
                        .orElseGet(() -> createCourseTag(name)))
                .collect(Collectors.toSet());
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

        Set<CourseTagEntity> tags = course.getCourseTags();
        repository.delete(course);

        for (CourseTagEntity tag : tags) {
            if (repository.countByCourseTagsContains(tag) == 0) {
                courseTagRepository.delete(tag);
            }
        }
    }

    @Transactional(readOnly = true)
    public CourseEntity findCourseById(Long courseId) {
        return repository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    @Transactional
    public CourseEntity updatePreview(Long courseId, MultipartFile file) throws IOException {
        CourseEntity course = findCourseById(courseId);

        FileEntity savedFile = fileService.saveFile(file);
        if (course.getPreview() != null) {
            fileService.deleteFile(course.getPreview().getId());
        }
        course.setPreview(savedFile);

        return repository.save(course);
    }

    @Transactional(readOnly = true)
    public List<CourseEntity> findAllCourses() {
        return repository.findAll();
    }

    @Override
    public CourseEntity getByHandle(String handle) {
        return repository.findByHandle(handle)
                .orElseThrow(() -> new CourseNotFoundException(handle));
    }
}
