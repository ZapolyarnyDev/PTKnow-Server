package io.github.zapolyarnydev.ptknow.entity.tag;

import io.github.zapolyarnydev.ptknow.exception.credentials.InvalidCredentialsException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.Future;

@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_sequence", allocationSize = 1)
    Long id;

    @Column(unique = true, nullable = false, updatable = false)
    String tagName;

    public CourseTagEntity(String tagName) {
        this.tagName = tagName;
    }

    @PrePersist
    public void checkTagName() {
        if(tagName == null || tagName.isBlank())
            throw new InvalidCredentialsException("Course tag name can't be null or blank");
    }
}
