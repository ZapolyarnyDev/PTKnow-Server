package io.github.zapolyarnydev.ptknow.dto.profile;

public record ProfileUpdateDTO(
        String fullName,
        String summary,
        String handle
) {}