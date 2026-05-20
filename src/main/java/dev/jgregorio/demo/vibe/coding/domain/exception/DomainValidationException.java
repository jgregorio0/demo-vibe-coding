package dev.jgregorio.demo.vibe.coding.domain.exception;

import lombok.Getter;
import java.util.List;

@Getter
public class DomainValidationException extends RuntimeException {
  private final List<ValidationError> errors;

  public DomainValidationException(final List<ValidationError> errors) {
    super("Validation failed");
    this.errors = errors;
  }
}
