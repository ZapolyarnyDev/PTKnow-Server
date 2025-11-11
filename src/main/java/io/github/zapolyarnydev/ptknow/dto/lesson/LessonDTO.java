package io.github.zapolyarnydev.ptknow.dto.lesson;

import io.github.zapolyarnydev.ptknow.entity.lesson.LessonState;
import io.github.zapolyarnydev.ptknow.entity.lesson.LessonType;

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