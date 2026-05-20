package dev.jgregorio.demo.vibe.coding.infrastructure.config;

import dev.jgregorio.demo.vibe.coding.domain.exception.DomainValidationException;
import dev.jgregorio.demo.vibe.coding.domain.exception.AlreadyExistsException;
import dev.jgregorio.demo.vibe.coding.domain.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleRocketNotFoundException(final NotFoundException ex) {
    log.error("Rocket not found: {}", ex.getMessage());
    final ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.NOT_FOUND.value())
        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
        .message(ex.getMessage())
        .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateRocketNameException(final AlreadyExistsException ex) {
    log.error("Duplicate rocket name error: {}", ex.getMessage());
    final ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.CONFLICT.value())
        .error(HttpStatus.CONFLICT.getReasonPhrase())
        .message(ex.getMessage())
        .build();
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  @ExceptionHandler(DomainValidationException.class)
  public ResponseEntity<ErrorResponse> handleDomainValidationException(final DomainValidationException ex) {
    log.error("Domain validation error: {}", ex.getMessage());
    final List<ValidationErrorResponse> validationErrors = ex.getErrors().stream()
        .map(err -> ValidationErrorResponse.builder()
            .domain(err.getDomain())
            .field(err.getField())
            .cause(err.getCause())
            .build())
        .collect(Collectors.toList());

    final ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .message("Validation failed")
        .errors(validationErrors)
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
    log.error("Method argument not valid error: {}", ex.getMessage());
    final List<ValidationErrorResponse> validationErrors = ex.getBindingResult().getFieldErrors().stream()
        .map(err -> ValidationErrorResponse.builder()
            .domain("rocket")
            .field(err.getField())
            .cause("INVALID")
            .build())
        .collect(Collectors.toList());

    final ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .message("Validation failed")
        .errors(validationErrors)
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(final Exception ex) {
    log.error("Unhandled exception occurred", ex);
    final ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .message("An unexpected error occurred")
        .build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
