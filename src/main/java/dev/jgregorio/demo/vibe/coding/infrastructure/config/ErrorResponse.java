package dev.jgregorio.demo.vibe.coding.infrastructure.config;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
  private final LocalDateTime timestamp;
  private final int status;
  private final String error;
  private final String message;
  private final List<ValidationErrorResponse> errors;
}
