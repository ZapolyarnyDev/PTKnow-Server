package io.github.zapolyarnydev.ptknow.repository.auth;

import io.github.zapolyarnydev.ptknow.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
