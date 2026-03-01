package ptknow.entity.course;

import lombok.*;
import ptknow.exception.credentials.InvalidCredentialsException;
import jakarta.persistence.*;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CourseTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_tag_id_generator")
    @SequenceGenerator(name = "course_tag_id_generator", sequenceName = "course_tag_sequence", allocationSize = 1)
    @EqualsAndHashCode.Include
    @Getter
    Long id;

    @Column(unique = true, nullable = false, updatable = false)
    @Getter
    String tagName;

    @ManyToMany(mappedBy = "courseTags")
    Set<CourseEntity> courses = new HashSet<>();

    public CourseTagEntity(String tagName) {
        this.tagName = tagName;
    }

    @PrePersist
    @PreUpdate
    public void checkTagName() {
        if(tagName == null || tagName.isBlank())
            throw new InvalidCredentialsException("Course tag name can't be null or blank");
    }

    public Set<CourseEntity> getCourses() {
        return Collections.unmodifiableSet(courses);
    }
}
