package ptknow.repository.course;

import ptknow.model.course.Course;
import ptknow.model.course.CourseTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByName(String name);
    Optional<Course> findByHandle(String handle);
    boolean existsByName(String name);
    boolean existsByHandle(String handle);
    int countByCourseTagsContains(CourseTag courseTagEntity);
    boolean existsByIdAndOwner_Id(Long id, UUID ownerId);
}

