package ptknow.model.enrollment;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ptknow.exception.credentials.InvalidCredentialsException;
import ptknow.model.auth.Auth;
import ptknow.model.course.Course;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enrollment_id_generator")
    @SequenceGenerator(name = "enrollment_id_generator", sequenceName = "enrollment_sequence", allocationSize = 1)
    @EqualsAndHashCode.Include
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    Auth user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false, updatable = false)
    Course course;

    @Column(nullable = false, updatable = false)
    Instant enrollSince;


    @PrePersist
    public void checkFields() {
        if(enrollSince == null)
            enrollSince = Instant.now();

        if(course == null)
            throw new InvalidCredentialsException("Course can't be null");
        if (user == null)
            throw new InvalidCredentialsException("User can't be null");
    }
}
