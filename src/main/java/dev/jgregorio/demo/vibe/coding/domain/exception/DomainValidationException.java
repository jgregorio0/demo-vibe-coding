package dev.jgregorio.demo.vibe.coding.domain.exception;

import java.util.List;
import lombok.Getter;

@Getter
public class DomainValidationException extends RuntimeException {
  private final List<ValidationError> errors;

  public DomainValidationException(final List<ValidationError> errors) {
    super("Validation failed");
    this.errors = errors;
  }
}
