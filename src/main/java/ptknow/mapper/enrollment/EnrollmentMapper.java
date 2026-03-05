package ptknow.mapper.enrollment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptknow.dto.enrollment.EnrollmentDTO;
import ptknow.model.enrollment.Enrollment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "enrollSince", target = "since")
    EnrollmentDTO toDTOFromEntity(Enrollment enrollment);

    List<EnrollmentDTO> mapEntityList(List<Enrollment> enrollments);

}
