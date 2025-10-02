package io.github.zapolyarnydev.ptknow.repository.course;

import io.github.zapolyarnydev.ptknow.entity.tag.CourseTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseTagRepository extends JpaRepository<CourseTagEntity, Long> {
    Optional<CourseTagEntity> findByName(String name);
}
