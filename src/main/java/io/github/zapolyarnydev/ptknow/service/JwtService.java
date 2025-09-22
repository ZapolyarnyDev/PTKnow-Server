package io.github.zapolyarnydev.ptknow.service;

import io.github.zapolyarnydev.ptknow.jwt.JwtTokens;
import io.github.zapolyarnydev.ptknow.entity.user.UserEntity;
import io.github.zapolyarnydev.ptknow.jwt.ClaimType;
import io.github.zapolyarnydev.ptknow.jwt.JwtClaim;
import io.github.zapolyarnydev.ptknow.properties.JwtProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JwtService {

    JwtProperties properties;
    JwtEncoder jwtEncoder;

    public String generateAccessToken(UserEntity user) {
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

    public String generateRefreshToken(UserEntity user) {
        var now = Instant.now();
        var claimSet = JwtClaimsSet.builder()
                .issuer(properties.getIssuer())
                .issuedAt(now)
                .expiresAt(properties.getRefreshTokenExpiryInstant())
                .subject(user.getUsername())
                .claim(JwtClaim.TYPE.getName(), ClaimType.REFRESH.getName())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();
    }

    public JwtTokens generateTokenPair(UserEntity entity) {
        return new JwtTokens(generateAccessToken(entity), generateRefreshToken(entity));
    }
}
