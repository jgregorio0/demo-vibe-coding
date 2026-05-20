package dev.jgregorio.demo.vibe.coding.domain.exception;

public class NotFoundException extends RuntimeException {
  public NotFoundException(final String message) {
    super(message);
  }
}
