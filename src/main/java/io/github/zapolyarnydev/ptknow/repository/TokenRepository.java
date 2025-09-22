package io.github.zapolyarnydev.ptknow.repository;

import io.github.zapolyarnydev.ptknow.entity.token.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByToken(String token);
}
