package dev.jgregorio.demo.vibe.coding.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Rocket {
  private final Long id;
  private final String name;
  private final RocketRange range;
  private final Integer capacity;
}
