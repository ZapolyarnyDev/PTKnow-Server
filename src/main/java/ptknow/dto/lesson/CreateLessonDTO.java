package ptknow.dto.lesson;

import ptknow.entity.lesson.LessonType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateLessonDTO(
        @NotBlank String name,
        String description,
        @NotNull Instant beginAt,
        @NotNull Instant endsAt,
        @NotNull LessonType type
) {}