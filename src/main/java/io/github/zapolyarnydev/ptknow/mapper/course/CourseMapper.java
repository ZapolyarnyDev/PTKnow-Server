package io.github.zapolyarnydev.ptknow.mapper.course;

import io.github.zapolyarnydev.ptknow.dto.course.CourseDTO;
import io.github.zapolyarnydev.ptknow.entity.course.CourseEntity;
import io.github.zapolyarnydev.ptknow.entity.course.CourseTagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(source = "courseTags", target = "tags")
    CourseDTO courseToDTO(CourseEntity entity);

    default List<String> mapTags(List<CourseTagEntity> tags) {
        return tags.stream().map(CourseTagEntity::getTagName).toList();
    }
}
