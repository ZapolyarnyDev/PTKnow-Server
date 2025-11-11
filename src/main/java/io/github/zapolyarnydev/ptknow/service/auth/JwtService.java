package io.github.zapolyarnydev.ptknow.service.auth;

import io.github.zapolyarnydev.ptknow.entity.token.RefreshTokenEntity;
import io.github.zapolyarnydev.ptknow.exception.token.InvalidTokenException;
import io.github.zapolyarnydev.ptknow.exception.token.TokenNotFoundException;
import io.github.zapolyarnydev.ptknow.jwt.JwtTokens;
import io.github.zapolyarnydev.ptknow.entity.auth.AuthEntity;
import io.github.zapolyarnydev.ptknow.jwt.ClaimType;
import io.github.zapolyarnydev.ptknow.jwt.JwtClaim;
import io.github.zapolyarnydev.ptknow.properties.JwtProperties;
import io.github.zapolyarnydev.ptknow.repository.auth.RefreshTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
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

    private String generateAccessToken(AuthEntity user) {
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

    private String generateRefreshToken(AuthEntity user) {
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

    public JwtTokens generateTokenPair(AuthEntity entity) {
        return new JwtTokens(generateAccessToken(entity), generateRefreshToken(entity));
    }

    public JwtTokens refresh(String refreshToken) throws TokenNotFoundException, InvalidTokenException {
        if(!isValid(refreshToken))
            throw new InvalidTokenException(refreshToken);

        RefreshTokenEntity entity = findToken(refreshToken);
        invalidate(entity);

        return generateTokenPair(entity.getUser());
    }

    public ResponseCookie tokenToCookie(String cookiePath, String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .maxAge(properties.getRefreshTokenExpiration())
                .sameSite("Strict")
                .path(cookiePath)
                .build();
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

    @Transactional
    public void invalidateUserTokens(AuthEntity user) {
        var tokens = tokenRepository.findAllByUserAndValidIsTrueAndExpireDateAfter(user, Instant.now());

        for (var token : tokens) {
            token.setValid(false);
        }

        tokenRepository.saveAll(tokens);
    }

    private RefreshTokenEntity findToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException(token));
    }
}
