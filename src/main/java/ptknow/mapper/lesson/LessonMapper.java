package ptknow.mapper.lesson;

import ptknow.dto.lesson.LessonDTO;
import ptknow.model.lesson.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "courseId", source = "course.id")
    LessonDTO toDTO(Lesson lesson);
}

