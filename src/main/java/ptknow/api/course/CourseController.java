package ptknow.api.course;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import ptknow.dto.course.CourseDTO;
import ptknow.dto.course.CreateCourseDTO;
import ptknow.entity.auth.AuthEntity;
import ptknow.entity.course.CourseEntity;
import ptknow.mapper.course.CourseMapper;
import ptknow.service.course.CourseService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v0/course")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseController {

    CourseService courseService;
    CourseMapper courseMapper;

    @GetMapping
    public ResponseEntity<List<CourseDTO>> get() {
        List<CourseDTO> courseDTOS = courseService.findAllCourses().stream()
                .map(courseMapper::courseToDTO)
                .toList();
        return ResponseEntity.ok(courseDTOS);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<CourseDTO> createCourse(
            @Valid @RequestPart("course") CreateCourseDTO dto,
            @RequestPart(value = "preview", required = false) MultipartFile preview,
            @AuthenticationPrincipal AuthEntity entity
            ) throws IOException {
        CourseEntity course = courseService.publishCourse(dto, entity, preview);

        return ResponseEntity.ok(courseMapper.courseToDTO(course));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable Long id) {
        CourseDTO course = courseMapper.courseToDTO(courseService.findCourseById(id));
        return ResponseEntity.ok(course);
    }

    @GetMapping("/handle/{handle}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable String handle) {
        CourseDTO course = courseMapper.courseToDTO(courseService.getByHandle(handle));
        return ResponseEntity.ok(course);
    }

    @PostMapping("/{id}/preview")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<CourseDTO> updatePreview(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal AuthEntity entity
    ) throws IOException {
        var updatedProfile = courseService.updatePreview(id, entity, file);
        var dto = courseMapper.courseToDTO(updatedProfile);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthEntity entity
            ) {
        courseService.deleteCourseById(id, entity);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
