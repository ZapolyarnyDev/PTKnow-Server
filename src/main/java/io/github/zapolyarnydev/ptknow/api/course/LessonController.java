package io.github.zapolyarnydev.ptknow.api.course;

import io.github.zapolyarnydev.ptknow.dto.lesson.CreateLessonDTO;
import io.github.zapolyarnydev.ptknow.dto.lesson.LessonDTO;
import io.github.zapolyarnydev.ptknow.mapper.lesson.LessonMapper;
import io.github.zapolyarnydev.ptknow.service.lesson.LessonService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<LessonDTO> createLesson(
            @PathVariable Long courseId,
            @Valid @RequestBody CreateLessonDTO dto
    ) {
        var lesson = lessonService.createLesson(courseId, dto);
        return ResponseEntity.ok(lessonMapper.toDTO(lesson));
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonDTO> getLesson(@PathVariable Long lessonId) {
        var lesson = lessonMapper.toDTO(lessonService.findById(lessonId));
        return ResponseEntity.ok(lesson);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<LessonDTO>> getLessonsByCourse(@PathVariable Long courseId) {
        var lessons = lessonService.findAllByCourse(courseId).stream()
                .map(lessonMapper::toDTO)
                .toList();
        return ResponseEntity.ok(lessons);
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        lessonService.deleteById(lessonId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
