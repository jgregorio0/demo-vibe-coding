package dev.jgregorio.demo.vibe.coding.infrastructure.config;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationErrorResponse {
  private final String domain;
  private final String field;
  private final String cause;
}
