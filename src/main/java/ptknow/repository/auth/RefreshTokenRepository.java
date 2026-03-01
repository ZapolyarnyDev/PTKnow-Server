package ptknow.repository.auth;

import ptknow.model.token.RefreshToken;
import ptknow.model.auth.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findAllByUserAndValidIsTrueAndExpireDateAfter(Auth user, Instant now);

}

