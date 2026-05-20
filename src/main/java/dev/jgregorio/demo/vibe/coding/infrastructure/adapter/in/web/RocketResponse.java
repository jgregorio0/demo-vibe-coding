package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.in.web;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class RocketResponse {
  private final Long id;
  private final String name;
  private final String range;
  private final Integer capacity;
}
