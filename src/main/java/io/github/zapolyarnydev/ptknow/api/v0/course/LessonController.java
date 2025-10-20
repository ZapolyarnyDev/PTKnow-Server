package io.github.zapolyarnydev.ptknow.api.v0.course;

import io.github.zapolyarnydev.ptknow.api.v0.ApiResponse;

import io.github.zapolyarnydev.ptknow.dto.course.CreateLessonDTO;
import io.github.zapolyarnydev.ptknow.dto.course.LessonDTO;
import io.github.zapolyarnydev.ptknow.mapper.course.LessonMapper;
import io.github.zapolyarnydev.ptknow.service.course.LessonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v0/lessons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonController {

    LessonService lessonService;
    LessonMapper lessonMapper;

    @PostMapping("/{courseId}")
    public ResponseEntity<ApiResponse<LessonDTO>> createLesson(
            @PathVariable Long courseId,
            @RequestBody CreateLessonDTO dto
    ) {
        var lesson = lessonService.createLesson(courseId, dto);
        return ResponseEntity.ok(ApiResponse.success("Занятие успешно создано", lessonMapper.toDTO(lesson)));
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<ApiResponse<LessonDTO>> getLesson(@PathVariable Long lessonId) {
        var lesson = lessonMapper.toDTO(lessonService.findById(lessonId));
        return ResponseEntity.ok(ApiResponse.success(null, lesson));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<LessonDTO>>> getLessonsByCourse(@PathVariable Long courseId) {
        var lessons = lessonService.findAllByCourse(courseId).stream()
                .map(lessonMapper::toDTO)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(null, lessons));
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<ApiResponse<Void>> deleteLesson(@PathVariable Long lessonId) {
        lessonService.deleteById(lessonId);
        return ResponseEntity.ok(ApiResponse.success("Занятие успешно удалено"));
    }
}
