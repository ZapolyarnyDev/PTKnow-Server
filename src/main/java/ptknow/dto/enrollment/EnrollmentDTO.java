package ptknow.dto.enrollment;

import java.time.Instant;
import java.util.UUID;

public record EnrollmentDTO(
        Long id,
        UUID userId,
        Long courseId,
        Instant since
) {
}
