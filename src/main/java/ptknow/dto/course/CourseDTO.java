package ptknow.dto.course;

import java.util.List;

public record CourseDTO(
        Long id,
        String name,
        String description,
        List<String> tags,
        String handle,
        String previewUrl
) { }


