package ptknow.model.profile;

import ptknow.model.file.File;
import ptknow.model.auth.Auth;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "user_profile")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String fullName;

    @OneToOne
    @JoinColumn(name = "user_id", updatable = false, unique = true)
    Auth user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    File avatar;

    @Column(length = 500)
    String summary;

    @Column(unique = true, nullable = false)
    String handle;
}

