package io.github.zapolyarnydev.ptknow.entity.course;

import io.github.zapolyarnydev.ptknow.exception.credentials.InvalidCredentialsException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_tag_id_generator")
    @SequenceGenerator(name = "course_tag_id_generator", sequenceName = "course_tag_sequence", allocationSize = 1)
    Long id;

    @Column(unique = true, nullable = false, updatable = false)
    String tagName;

    @ManyToMany(mappedBy = "courseTags")
    List<CourseEntity> courses;

    public CourseTagEntity(String tagName) {
        this.tagName = tagName;
    }

    @PrePersist
    @PreUpdate
    public void checkTagName() {
        if(tagName == null || tagName.isBlank())
            throw new InvalidCredentialsException("Course tag name can't be null or blank");
    }
}
