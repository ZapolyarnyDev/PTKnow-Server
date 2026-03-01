package ptknow.mapper.course;

import ptknow.dto.course.CourseDTO;
import ptknow.model.course.Course;
import ptknow.model.course.CourseTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(source = "courseTags", target = "tags")
    @Mapping(source = "preview.id", target = "previewUrl", qualifiedByName = "mapPreviewIdToUrl")
    CourseDTO courseToDTO(Course entity);

    default List<String> mapCourseTags(Set<CourseTag> tags) {
        return tags.stream().map(CourseTag::getTagName).toList();
    }

    @Named("mapPreviewIdToUrl")
    default String mapPreviewIdToUrl(UUID previewId) {
        if (previewId == null) return null;
        return "/v0/files/" + previewId;
    }
}

