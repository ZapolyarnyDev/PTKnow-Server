package io.github.zapolyarnydev.ptknow.repository;

import io.github.zapolyarnydev.ptknow.entity.token.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);
}
