package io.github.zapolyarnydev.ptknow.service;

import io.github.zapolyarnydev.ptknow.entity.token.RefreshTokenEntity;
import io.github.zapolyarnydev.ptknow.exception.token.InvalidTokenException;
import io.github.zapolyarnydev.ptknow.exception.token.TokenNotFoundException;
import io.github.zapolyarnydev.ptknow.jwt.JwtTokens;
import io.github.zapolyarnydev.ptknow.entity.user.UserEntity;
import io.github.zapolyarnydev.ptknow.jwt.ClaimType;
import io.github.zapolyarnydev.ptknow.jwt.JwtClaim;
import io.github.zapolyarnydev.ptknow.properties.JwtProperties;
import io.github.zapolyarnydev.ptknow.repository.RefreshTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JwtService {

    JwtProperties properties;
    JwtEncoder jwtEncoder;
    RefreshTokenRepository tokenRepository;

    private String generateAccessToken(UserEntity user) {
        var now = Instant.now();
        var claimSet = JwtClaimsSet.builder()
                .issuer(properties.getIssuer())
                .issuedAt(now)
                .expiresAt(properties.getAccessTokenExpiryInstant())
                .subject(user.getUsername())
                .claim(JwtClaim.TYPE.getName(), ClaimType.ACCESS.getName())
                .claim(JwtClaim.ROLE.getName(), user.getRole().authorityName())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();
    }

    private String generateRefreshToken(UserEntity user) {
        var now = Instant.now();
        Instant expiresAt = properties.getRefreshTokenExpiryInstant();

        var claimSet = JwtClaimsSet.builder()
                .issuer(properties.getIssuer())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.getUsername())
                .claim(JwtClaim.TYPE.getName(), ClaimType.REFRESH.getName())
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();

        var entity = RefreshTokenEntity.builder()
                .token(token)
                .expireDate(expiresAt)
                .user(user)
                .build();

        tokenRepository.save(entity);
        return token;
    }

    public JwtTokens generateTokenPair(UserEntity entity) {
        return new JwtTokens(generateAccessToken(entity), generateRefreshToken(entity));
    }

    public JwtTokens refresh(String refreshToken) throws TokenNotFoundException, InvalidTokenException {
        if(!isValid(refreshToken))
            throw new InvalidTokenException(refreshToken);

        RefreshTokenEntity entity = findToken(refreshToken);
        invalidate(entity);

        return generateTokenPair(entity.getUser());
    }

    public boolean isValid(String token) {
        var entity = findToken(token);

        return entity.isValid() && entity.getExpireDate().isAfter(Instant.now());
    }

    @Transactional
    public void invalidate(RefreshTokenEntity token) {
        token.setValid(false);
        log.info("Токен {} инвалидирован. ID: {}", token.getToken(), token.getId());
    }

    private RefreshTokenEntity findToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException(token));
    }
}
