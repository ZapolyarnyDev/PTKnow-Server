package ptknow.repository.course;

import ptknow.entity.course.CourseEntity;
import ptknow.entity.course.CourseTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    Optional<CourseEntity> findByName(String name);
    Optional<CourseEntity> findByHandle(String handle);
    boolean existsByName(String name);
    boolean existsByHandle(String handle);
    int countByCourseTagsContains(CourseTagEntity courseTagEntity);
    boolean existsByIdAndOwner_Id(Long id, UUID ownerId);
}
