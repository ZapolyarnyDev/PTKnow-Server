package io.github.zapolyarnydev.ptknow.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "auth_data")
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    UUID id;

    @Column(nullable = false)
    String fullName;

    @Column(updatable = false, unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    @Setter
    String password;

    @Column(nullable = false, updatable = false)
    Instant registeredAt;

    @Builder
    public UserEntity(String fullName, String email, String password, Instant registeredAt) {
        this.fullName = fullName;
        this.registeredAt = registeredAt;
        this.password = password;
        this.email = email;
    }

    @PrePersist
    public void prePersist() {
        if(registeredAt == null)
            registeredAt = Instant.now();
    }
}
