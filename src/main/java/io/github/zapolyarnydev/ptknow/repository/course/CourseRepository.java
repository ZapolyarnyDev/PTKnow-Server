package io.github.zapolyarnydev.ptknow.repository.course;

import io.github.zapolyarnydev.ptknow.entity.course.CourseEntity;
import io.github.zapolyarnydev.ptknow.entity.course.CourseTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    Optional<CourseEntity> findByName(String name);
    Optional<CourseEntity> findByHandle(String handle);
    boolean existsByName(String name);
    boolean existsByHandle(String handle);
    int countByCourseTagsContains(CourseTagEntity courseTagEntity);
}
