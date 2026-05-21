package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jgregorio.demo.vibe.coding.domain.exception.DomainValidationException;
import dev.jgregorio.demo.vibe.coding.domain.exception.ValidationError;
import dev.jgregorio.demo.vibe.coding.domain.model.Rocket;
import dev.jgregorio.demo.vibe.coding.domain.model.RocketRange;
import org.junit.jupiter.api.Test;

class RocketApiMapperTest {

  private final RocketApiMapper mapper = new RocketApiMapperImpl();

  @Test
  void toDomain_shouldMapValidRequest_whenInputIsValid() {
    // Given
    final RocketRequest request =
        RocketRequest.builder().name("Falcon").range("orbital").capacity(5).build();

    // When
    final Rocket result = mapper.toDomain(request);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isNull();
    assertThat(result.getName()).isEqualTo("Falcon");
    assertThat(result.getRange()).isEqualTo(RocketRange.ORBITAL);
    assertThat(result.getCapacity()).isEqualTo(5);
  }

  @Test
  void toDomain_shouldMapNullRangeToNull_whenRangeIsNullOrEmpty() {
    // Given
    final RocketRequest request =
        RocketRequest.builder().name("Falcon").range("").capacity(5).build();

    // When
    final Rocket result = mapper.toDomain(request);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getRange()).isNull();
  }

  @Test
  void toDomain_shouldThrowDomainValidationException_whenRangeIsInvalid() {
    // Given
    final RocketRequest request =
        RocketRequest.builder().name("Falcon").range("galactic").capacity(5).build();

    // When & Then
    assertThatThrownBy(() -> mapper.toDomain(request))
        .isInstanceOf(DomainValidationException.class)
        .satisfies(
            ex -> {
              final DomainValidationException valEx = (DomainValidationException) ex;
              assertThat(valEx.getErrors()).hasSize(1);
              final ValidationError error = valEx.getErrors().get(0);
              assertThat(error.getDomain()).isEqualTo("rocket");
              assertThat(error.getField()).isEqualTo("range");
              assertThat(error.getCause()).isEqualTo("INVALID");
            });
  }

  @Test
  void toResponse_shouldMapDomainToResponse_whenInputIsValid() {
    // Given
    final Rocket rocket =
        Rocket.builder().id(1L).name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();

    // When
    final RocketResponse response = mapper.toResponse(rocket);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(1L);
    assertThat(response.getName()).isEqualTo("Falcon");
    assertThat(response.getRange()).isEqualTo("ORBITAL");
    assertThat(response.getCapacity()).isEqualTo(5);
  }
}
