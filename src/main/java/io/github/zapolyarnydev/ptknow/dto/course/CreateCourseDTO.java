package io.github.zapolyarnydev.ptknow.dto.course;

import java.util.Set;

public record CreateCourseDTO(
        String name,
        String description,
        Set<String> tags
) { }
