package io.github.zapolyarnydev.ptknow.dto.course;

import java.util.List;

public record CreateCourseDTO(
        String name,
        String description,
        List<String> tags
) { }
