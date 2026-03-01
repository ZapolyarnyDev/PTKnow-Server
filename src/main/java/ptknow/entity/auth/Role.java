package ptknow.entity.auth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST"),
    STUDENT("ROLE_STUDENT"),
    TEACHER("ROLE_TEACHER"),
    ADMIN("ROLE_ADMIN");

    private final String authorityName;

    public String authorityName() {
        return this.authorityName;
    }
}
