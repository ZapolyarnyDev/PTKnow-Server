package ptknow.mapper.lesson;

import ptknow.dto.lesson.LessonDTO;
import ptknow.entity.lesson.LessonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "courseId", source = "course.id")
    LessonDTO toDTO(LessonEntity lesson);
}
