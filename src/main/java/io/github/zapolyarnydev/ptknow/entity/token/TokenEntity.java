package io.github.zapolyarnydev.ptknow.entity.token;

import io.github.zapolyarnydev.ptknow.exception.credentials.InvalidCredentialsException;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TokenEntity {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_sequence", allocationSize = 1)
    Long id;

    @Column(unique = true, nullable = false, updatable = false)
    String token;

    @Column(nullable = false, updatable = false)
    Instant expireDate;

    @Column(nullable = false)
    @Setter
    boolean isValid = true;


    @PrePersist
    public void checkFields() {
        if(token == null)
            throw new InvalidCredentialsException("Token entity require token data");

        if(expireDate == null)
            throw new InvalidCredentialsException("Token entity require expire date");
    }

}
