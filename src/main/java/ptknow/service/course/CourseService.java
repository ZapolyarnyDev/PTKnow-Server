package ptknow.service.course;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ptknow.dto.course.CreateCourseDTO;
import ptknow.model.auth.Auth;
import ptknow.model.auth.Role;
import ptknow.model.course.Course;
import ptknow.model.course.CourseTag;
import ptknow.model.file.File;
import ptknow.exception.course.*;
import ptknow.exception.user.UserNotFoundException;
import ptknow.generator.handle.HandleGenerator;
import ptknow.repository.auth.AuthRepository;
import ptknow.repository.course.CourseRepository;
import ptknow.repository.course.CourseTagRepository;
import ptknow.service.HandleService;
import ptknow.service.OwnershipService;
import ptknow.service.file.FileService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CourseService implements HandleService<Course>, OwnershipService<Long> {

    AuthRepository authRepository;
    CourseRepository repository;
    CourseTagRepository courseTagRepository;
    HandleGenerator handleGenerator;
    FileService fileService;

    @Transactional
    public Course publishCourse(CreateCourseDTO dto, Auth initiator, MultipartFile preview) throws IOException {
        if (repository.existsByName(dto.name())) {
            throw new CourseAlreadyExists(dto.name());
        }

        String handle = handleGenerator.generate(repository::existsByHandle);

        File previewFile = null;
        if (preview != null && !preview.isEmpty()) {
            previewFile = fileService.saveFile(preview);
        }

        var entity = Course.builder()
                .name(dto.name())
                .description(dto.description())
                .courseTags(courseTagsFromNames(dto.tags()))
                .handle(handle)
                .preview(previewFile)
                .owner(initiator)
                .build();

        initiator.addOwnedCourse(entity);

        repository.save(entity);
        return entity;
    }


    @Transactional
    public Set<CourseTag> courseTagsFromNames(Set<String> names) {
        return names.stream()
                .map(name -> courseTagRepository.findByTagName(name)
                        .orElseGet(() -> createCourseTag(name)))
                .collect(Collectors.toSet());
    }

    @Transactional
    public CourseTag createCourseTag(String name) {
        if (courseTagRepository.existsByTagName(name))
            throw new CourseTagAlreadyExists(name);

        var entity = new CourseTag(name);
        courseTagRepository.save(entity);

        return entity;
    }

    @Transactional
    public void deleteCourseById(Long courseId, Auth initiator) {
        var course = findCourseById(courseId);

        if(initiator.getRole() != Role.ADMIN && !course.getOwner().equals(initiator))
            throw new CourseNotOwnedByUserException(initiator.getId());

        Set<CourseTag> tags = course.getCourseTags();
        repository.delete(course);

        for (CourseTag tag : tags) {
            if (repository.countByCourseTagsContains(tag) == 0) {
                courseTagRepository.delete(tag);
            }
        }
    }

    @Transactional(readOnly = true)
    public Course findCourseById(Long courseId) {
        return repository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    public boolean canEdit(Course course, Auth auth) {
        return auth.getRole() == Role.ADMIN ||
                course.getOwner().equals(auth) ||
                course.hasEditor(auth);
    }

    @Transactional
    public Course updatePreview(Long courseId, Auth initiator, MultipartFile file) throws IOException {
        Course course = findCourseById(courseId);

        if(!canEdit(course, initiator))
            throw new CourseCannotBeEditByUserException(initiator.getId());

        File savedFile = fileService.saveFile(file);
        if (course.getPreview() != null) {
            fileService.deleteFile(course.getPreview().getId());
        }
        course.setPreview(savedFile);

        return repository.save(course);
    }

    @Transactional(readOnly = true)
    public List<Course> findAllCourses() {
        return repository.findAll();
    }

    @Override
    public Course getByHandle(String handle) {
        return repository.findByHandle(handle)
                .orElseThrow(() -> new CourseNotFoundException(handle));
    }

    @Override
    public boolean isOwner(Long resourceId, Auth auth) {
        return repository.existsByIdAndOwner_Id(resourceId, auth.getId());
    }

    @Override
    public Auth getOwner(Long resourceId) {
        Optional<Course> result = repository.findById(resourceId);

        if(result.isEmpty())
            throw new CourseNotFoundException(resourceId);

        return result.get().getOwner();
    }

    public Course addEditor(Long courseId, Auth initiator, UUID targetId) {
        Course course = findCourseById(courseId);

        if(initiator.getRole() != Role.ADMIN && !course.getOwner().equals(initiator))
            throw new CourseNotOwnedByUserException(initiator.getId());

        Auth target = authRepository.findById(targetId)
                .orElseThrow(() -> new UserNotFoundException(targetId));

        course.addEditor(target);

        authRepository.save(target);
        return repository.save(course);
    }


    public Course removeEditor(Long courseId, Auth initiator, UUID targetId) {
        Course course = findCourseById(courseId);

        if(initiator.getRole() != Role.ADMIN && !course.getOwner().equals(initiator))
            throw new CourseNotOwnedByUserException(initiator.getId());

        Auth target = authRepository.findById(targetId)
                .orElseThrow(() -> new UserNotFoundException(targetId));

        course.removeEditor(target);

        authRepository.save(target);
        return repository.save(course);
    }
}

