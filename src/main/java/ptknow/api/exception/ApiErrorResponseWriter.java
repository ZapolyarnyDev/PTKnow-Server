package ptknow.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApiErrorResponseWriter {

    private final ObjectMapper objectMapper;

    public void write(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpStatus status,
            String code,
            String message
    ) throws IOException {
        response.setStatus(status.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiError apiError = ApiError.of(status, code, request.getRequestURI(), message);
        objectMapper.writeValue(response.getOutputStream(), apiError);
    }
}

