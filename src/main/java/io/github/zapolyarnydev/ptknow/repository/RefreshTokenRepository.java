package io.github.zapolyarnydev.ptknow.repository;

import io.github.zapolyarnydev.ptknow.entity.token.RefreshTokenEntity;
import io.github.zapolyarnydev.ptknow.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

    List<RefreshTokenEntity> findAllByUserAndValidIsTrueAndExpireDateAfter(UserEntity user, Instant now);

}
