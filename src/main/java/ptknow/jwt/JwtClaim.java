package ptknow.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtClaim {

    TYPE("type"),
    ROLE("role");

    private final String name;
}
