package ptknow.dto.lesson;

import ptknow.entity.lesson.LessonState;
import ptknow.entity.lesson.LessonType;

import java.time.Instant;

public record LessonDTO(
        Long id,
        String name,
        String description,
        Instant beginAt,
        Instant endsAt,
        LessonState state,
        Long courseId,
        LessonType type
) {}