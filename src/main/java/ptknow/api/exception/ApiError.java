package ptknow.api.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

public record ApiError(
        Instant timestamp,
        String error,
        String code,
        String path,
        String message,
        Map<String, String> fieldErrors
) {

    public static ApiError of(HttpStatus status, String code, String path, String message) {
        return of(status, code, path, message, Map.of());
    }

    public static ApiError of(HttpStatus status, String code, String path, String message, Map<String, String> fieldErrors) {
        return new ApiError(
                Instant.now(),
                status.getReasonPhrase(),
                code,
                path,
                message,
                Collections.unmodifiableMap(fieldErrors)
        );
    }
}
