package ptknow.repository.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptknow.model.enrollment.Enrollment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByUser_IdAndCourse_Id(UUID userId, Long courseId);

    Optional<Enrollment> findByUser_IdAndCourse_Id(UUID userId, Long courseId);

    void deleteByUser_IdAndCourse_Id(UUID userId, Long courseId);

    List<Enrollment> findAllByCourse_Id(Long courseId);

    int countByCourse_Id(Long courseId);
}
