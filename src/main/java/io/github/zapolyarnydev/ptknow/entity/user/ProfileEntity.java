package io.github.zapolyarnydev.ptknow.entity.user;

import io.github.zapolyarnydev.ptknow.entity.file.FileEntity;
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
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", updatable = false, unique = true)
    UserEntity user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    FileEntity avatar;

    @Column(length = 500)
    String summary;

    @Column(unique = true, nullable = false)
    String handle;
}
