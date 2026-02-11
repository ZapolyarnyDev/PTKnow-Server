package ptknow.mapper.course;

import ptknow.dto.course.CourseDTO;
import ptknow.entity.course.CourseEntity;
import ptknow.entity.course.CourseTagEntity;
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
    CourseDTO courseToDTO(CourseEntity entity);

    default List<String> mapCourseTags(Set<CourseTagEntity> tags) {
        return tags.stream().map(CourseTagEntity::getTagName).toList();
    }

    @Named("mapPreviewIdToUrl")
    default String mapPreviewIdToUrl(UUID previewId) {
        if (previewId == null) return null;
        return "/v0/files/" + previewId;
    }
}
