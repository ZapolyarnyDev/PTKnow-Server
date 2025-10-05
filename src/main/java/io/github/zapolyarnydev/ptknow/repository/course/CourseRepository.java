package io.github.zapolyarnydev.ptknow.repository.course;

import io.github.zapolyarnydev.ptknow.entity.course.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    Optional<CourseEntity> findByName(String name);
    boolean existsByName(String name);
}
