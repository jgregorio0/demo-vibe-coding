package dev.jgregorio.demo.vibe.coding.domain.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationError {
  private final String domain;
  private final String field;
  private final String cause;
}
