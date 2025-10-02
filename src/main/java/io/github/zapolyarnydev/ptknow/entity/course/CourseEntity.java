package io.github.zapolyarnydev.ptknow.entity.course;

import io.github.zapolyarnydev.ptknow.exception.credentials.InvalidCredentialsException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_id_generator")
    @SequenceGenerator(name = "course_id_generator", sequenceName = "course_sequence", allocationSize = 1)
    Long id;

    @Column(unique = true, nullable = false)
    String name;

    String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "course_tags_mapping",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    List<CourseTagEntity> courseTags;

    @Builder
    public CourseEntity(List<CourseTagEntity> courseTags, String name, String description) {
        this.courseTags = courseTags;
        this.name = name;
        this.description = description;
    }

    @PrePersist
    public void checkCourseName() {
        if(name == null || name.isBlank())
            throw new InvalidCredentialsException("Course name can't be null or blank");
    }
}
