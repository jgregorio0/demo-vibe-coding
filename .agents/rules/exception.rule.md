---
trigger: glob
globs: "*ExceptionHandler.java, *Advice.java, *Exception.java"
---

## Exception Handling Rules

- Custom domain exceptions must extend `RuntimeException`.
- Implement a Global Exception Handler using `@ControllerAdvice` and `@ExceptionHandler`.
- Ensure all exceptions map to appropriate clean HTTP Status Codes in REST controllers.
- Maintain a consistent JSON error response structure across the application.
- ValidationError is composed of: `(domain, field, cause)`.
    - Domain is given by `*Validator.getDomain()`.
    - Field is given by a static constant in the validator (e.g., `FIELD_START_TIME`).
    - Cause is given by a ValidationError constant (e.g., `REQUIRED`, `ALREADY_EXISTS`).
- Example:

```java
package dev.jgregorio.demo.vibe.coding.domain.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message) {
        super(message);
    }
}
```

- *Example:*

```java

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRocketNotFoundException(final NotFoundException ex) {
        log.error("Rocket not found: {}", ex.getMessage());
        final ErrorResponse errorResponse =
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.NOT_FOUND.value())
                        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .message(ex.getMessage())
                        .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
```
