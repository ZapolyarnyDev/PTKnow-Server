package ptknow.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record CreateCourseDTO(
        @NotBlank String name,
        @NotBlank String description,
        @NotEmpty Set<String> tags
) { }

