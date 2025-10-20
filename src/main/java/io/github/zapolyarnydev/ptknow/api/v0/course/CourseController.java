package io.github.zapolyarnydev.ptknow.api.v0.course;

import io.github.zapolyarnydev.ptknow.api.v0.ApiResponse;
import io.github.zapolyarnydev.ptknow.dto.course.CourseDTO;
import io.github.zapolyarnydev.ptknow.dto.course.CreateCourseDTO;
import io.github.zapolyarnydev.ptknow.entity.course.CourseEntity;
import io.github.zapolyarnydev.ptknow.mapper.CourseMapper;
import io.github.zapolyarnydev.ptknow.service.course.CourseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v0/course")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseController {

    CourseService courseService;
    CourseMapper courseMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseDTO>>> get() {
        List<CourseDTO> courseDTOS = courseService.findAllCourses().stream()
                .map(courseMapper::courseToDTO)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Список всех курсов успешно отправлен", courseDTOS));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseDTO>> createCourse(@RequestBody CreateCourseDTO dto) {
        CourseEntity course = courseService.publishCourse(dto);

        String message = String.format("Курс %s успешно опубликован", course.getName());
        var response = ApiResponse.success(message, courseMapper.courseToDTO(course));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDTO>> getCourse(@PathVariable Long id) {
        CourseDTO course = courseMapper.courseToDTO(courseService.findCourseById(id));
        return ResponseEntity.ok(ApiResponse.success(null, course));
    }

    @GetMapping("/{handle}")
    public ResponseEntity<ApiResponse<CourseDTO>> getCourse(@PathVariable String handle) {
        CourseDTO course = courseMapper.courseToDTO(courseService.getByHandle(handle));
        return ResponseEntity.ok(ApiResponse.success(null, course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
