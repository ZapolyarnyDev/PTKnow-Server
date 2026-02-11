package ptknow.repository.auth;

import ptknow.entity.token.RefreshTokenEntity;
import ptknow.entity.auth.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

    List<RefreshTokenEntity> findAllByUserAndValidIsTrueAndExpireDateAfter(AuthEntity user, Instant now);

}
