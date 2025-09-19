package io.github.zapolyarnydev.ptknow.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Configuration
@ConfigurationProperties(prefix = "security.jwt")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtProperties {
    @Getter
    @NotBlank
    String secretKey;

    @NotNull
    Duration accessTokenExpiration = Duration.ofMinutes(15);

    @NotNull
    Duration refreshTokenExpiration = Duration.ofDays(7);

    public Instant getAccessTokenExpiryInstant() {
        return Instant.now().plus(accessTokenExpiration);
    }

    public Instant getRefreshTokenExpiryInstant() {
        return Instant.now().plus(refreshTokenExpiration);
    }
}
