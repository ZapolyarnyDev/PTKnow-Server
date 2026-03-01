package ptknow.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ptknow.exception.course.*;
import ptknow.exception.credentials.InvalidCredentialsException;
import ptknow.exception.email.EmailAlreadyUsedException;
import ptknow.exception.email.EmailNotFoundException;
import ptknow.exception.file.FileNotFoundException;
import ptknow.exception.token.InvalidTokenException;
import ptknow.exception.token.TokenNotFoundException;
import ptknow.exception.user.UserNotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            EmailAlreadyUsedException.class,
            CourseAlreadyExists.class,
            CourseTagAlreadyExists.class
    })
    public ResponseEntity<ApiError> handleConflict(RuntimeException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "resource_already_exists", req, ex.getMessage());
    }

    @ExceptionHandler({
            TokenNotFoundException.class,
            UserNotFoundException.class,
            FileNotFoundException.class,
            CourseNotFoundException.class,
            LessonNotFoundException.class,
            EmailNotFoundException.class,
            NoHandlerFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFound(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "resource_not_found", req, ex.getMessage());
    }

    @ExceptionHandler({
            InvalidCredentialsException.class,
            InvalidTokenException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MissingRequestValueException.class,
            ConstraintViolationException.class,
            HandlerMethodValidationException.class,
            MultipartException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "bad_request", req, ex.getMessage());
    }

    @ExceptionHandler({
            CourseCannotBeEditByUserException.class,
            CourseNotOwnedByUserException.class
    })
    public ResponseEntity<ApiError> handleForbidden(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "forbidden", req, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error instanceof FieldError fieldError ? fieldError.getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return build(HttpStatus.BAD_REQUEST, "validation_failed", req, "Validation failed", errors);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiError> handleMaxUploadSize(MaxUploadSizeExceededException ex, HttpServletRequest req) {
        return build(HttpStatus.PAYLOAD_TOO_LARGE, "payload_too_large", req, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "forbidden", req, ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiError> handleIo(IOException ex, HttpServletRequest req) {
        log.error("I/O error on {}", req.getRequestURI(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "io_error", req, "I/O error while processing request");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnknown(Exception ex, HttpServletRequest req) {
        log.error("Unhandled error on {}", req.getRequestURI(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "internal_error", req, "Internal server error");
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String code, HttpServletRequest req, String message) {
        return ResponseEntity.status(status)
                .body(ApiError.of(status, code, req.getRequestURI(), defaultMessage(status, message)));
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String code, HttpServletRequest req, String message, Map<String, String> fieldErrors) {
        return ResponseEntity.status(status)
                .body(ApiError.of(status, code, req.getRequestURI(), defaultMessage(status, message), fieldErrors));
    }

    private String defaultMessage(HttpStatus status, String message) {
        if (message == null || message.isBlank()) {
            return status.getReasonPhrase();
        }

        return message;
    }
}

