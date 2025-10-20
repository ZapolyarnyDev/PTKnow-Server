package io.github.zapolyarnydev.ptknow.entity.course;

import io.github.zapolyarnydev.ptknow.entity.file.FileEntity;
import io.github.zapolyarnydev.ptknow.entity.lesson.LessonEntity;
import io.github.zapolyarnydev.ptknow.exception.credentials.InvalidCredentialsException;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_id_generator")
    @SequenceGenerator(name = "course_id_generator", sequenceName = "course_sequence", allocationSize = 1)
    @EqualsAndHashCode.Include
    Long id;

    @Column(unique = true, nullable = false)
    String name;

    String description;

    @Column(unique = true, nullable = false)
    String handle;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "course_tags_mapping",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    Set<CourseTagEntity> courseTags;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    Set<LessonEntity> lessons;

    @Setter
    @Column(nullable = false)
    int maxUsersAmount = 10;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preview_id")
    @Getter
    @Setter
    FileEntity preview;

    @PrePersist
    @PreUpdate
    public void checkCourseFields() {
        if(name == null || name.isBlank())
            throw new InvalidCredentialsException("Course name can't be null or blank");
        if (handle == null || handle.isBlank())
            throw new InvalidCredentialsException("Course handle can't be null or blank");
        if(maxUsersAmount <= 0)
            throw new InvalidCredentialsException("Course must be open to at least 1 person");
    }
}
