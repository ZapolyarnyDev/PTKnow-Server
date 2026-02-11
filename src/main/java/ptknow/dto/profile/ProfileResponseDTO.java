package ptknow.dto.profile;

public record ProfileResponseDTO(
        String fullName,
        String summary,
        String handle,
        String avatarUrl
) {}
