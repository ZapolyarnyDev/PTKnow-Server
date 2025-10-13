package io.github.zapolyarnydev.ptknow.entity.lesson;

import io.github.zapolyarnydev.ptknow.entity.course.CourseEntity;
import io.github.zapolyarnydev.ptknow.exception.credentials.InvalidCredentialsException;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "lesson")
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LessonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lesson_id_generator")
    @SequenceGenerator(name = "lesson_id_generator", sequenceName = "lesson_sequence", allocationSize = 1)
    @EqualsAndHashCode.Include
    Long id;

    @Column(nullable = false)
    String name;

    String description;

    @Column(nullable = false)
    Instant beginAt;

    @Column(nullable = false)
    Instant endsAt;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    LessonState state = LessonState.PLANNED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    CourseEntity course;

    @Builder
    public LessonEntity(String name, String description, Instant beginAt, Instant endsAt, CourseEntity course) {
        this.name = name;
        this.description = description;
        this.beginAt = beginAt;
        this.endsAt = endsAt;
        this.course = course;
    }

    @PrePersist
    @PreUpdate
    public void validateLesson() {
        if(name == null || name.isBlank())
            throw new InvalidCredentialsException("Lesson name can't be null or blank");
        if (beginAt == null || endsAt == null) {
            throw new InvalidCredentialsException("Lesson time can't be null");
        }
        if (endsAt.isBefore(beginAt)) {
            throw new InvalidCredentialsException("Lesson end time can't be before start time");
        }
        if (course == null) {
            throw new InvalidCredentialsException("Lesson must be linked to a course");
        }
    }
}
