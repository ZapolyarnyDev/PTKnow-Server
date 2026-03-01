package ptknow.repository.course;

import ptknow.model.course.CourseTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseTagRepository extends JpaRepository<CourseTag, Long> {
    Optional<CourseTag> findByTagName(String name);
    boolean existsByTagName(String name);
}

