package io.github.zapolyarnydev.ptknow.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private final String authorityName;

    public String authorityName() {
        return this.authorityName;
    }
}
