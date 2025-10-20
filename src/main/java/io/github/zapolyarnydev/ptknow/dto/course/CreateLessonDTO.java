package io.github.zapolyarnydev.ptknow.dto.course;

import java.time.Instant;

public record CreateLessonDTO(
        String name,
        String description,
        Instant beginAt,
        Instant endsAt
) {}