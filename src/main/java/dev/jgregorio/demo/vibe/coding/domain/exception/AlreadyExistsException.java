package dev.jgregorio.demo.vibe.coding.domain.exception;

public class AlreadyExistsException extends RuntimeException {
  public AlreadyExistsException(final String message) {
    super(message);
  }
}
