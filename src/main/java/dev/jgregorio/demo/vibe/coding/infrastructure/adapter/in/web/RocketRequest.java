package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.in.web;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import tools.jackson.databind.annotation.JsonDeserialize;

@Getter
@Builder
@JsonDeserialize(builder = RocketRequest.RocketRequestBuilder.class)
public class RocketRequest {

  @NotBlank(message = "Name is required")
  private final String name;

  @NotBlank(message = "Range is required")
  private final String range;

  @NotNull(message = "Capacity is required")
  @Min(value = 1, message = "Capacity must be at least 1")
  @Max(value = 10, message = "Capacity must be at most 10")
  private final Integer capacity;

  @tools.jackson.databind.annotation.JsonPOJOBuilder(withPrefix = "")
  public static class RocketRequestBuilder {}
}
