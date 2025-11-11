package io.github.zapolyarnydev.ptknow.mapper.lesson;

import io.github.zapolyarnydev.ptknow.dto.lesson.LessonDTO;
import io.github.zapolyarnydev.ptknow.entity.lesson.LessonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "courseId", source = "course.id")
    LessonDTO toDTO(LessonEntity lesson);
}
