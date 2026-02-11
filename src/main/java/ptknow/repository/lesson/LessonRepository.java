package ptknow.repository.lesson;

import ptknow.entity.lesson.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Long> {
    List<LessonEntity> getAllByCourse_Id(Long courseId);
}
