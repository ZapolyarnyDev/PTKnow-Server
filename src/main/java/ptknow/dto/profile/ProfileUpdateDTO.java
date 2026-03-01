package ptknow.dto.profile;

import org.hibernate.validator.constraints.Length;

public record ProfileUpdateDTO(
        String fullName,
        String summary,
        @Length(min = 8, max = 32)
        String handle
) {}
