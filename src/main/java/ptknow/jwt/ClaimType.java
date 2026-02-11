package ptknow.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ClaimType {

    ACCESS("access"),
    REFRESH("refresh");

    private final String name;
}
