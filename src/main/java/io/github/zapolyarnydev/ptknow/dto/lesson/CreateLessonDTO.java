package io.github.zapolyarnydev.ptknow.dto.lesson;

import io.github.zapolyarnydev.ptknow.entity.lesson.LessonType;

import java.time.Instant;

public record CreateLessonDTO(
        String name,
        String description,
        Instant beginAt,
        Instant endsAt,
        LessonType type
) {}