package ptknow.model.token;

import ptknow.model.auth.Auth;
import ptknow.exception.credentials.InvalidCredentialsException;
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
public class RefreshToken {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_id_generator")
    @SequenceGenerator(name = "refresh_token_id_generator", sequenceName = "refresh_token_sequence", allocationSize = 1)
    Long id;

    @Column(unique = true, nullable = false, updatable = false)
    String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    Auth user;

    @Column(nullable = false, updatable = false)
    Instant expireDate;

    @Column(nullable = false)
    @Setter
    @Builder.Default
    boolean valid = true;


    @PrePersist
    public void checkFields() {
        if(token == null)
            throw new InvalidCredentialsException("Token entity require token data");

        if(expireDate == null)
            throw new InvalidCredentialsException("Token entity require expire date");
    }

}

