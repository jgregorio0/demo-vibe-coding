package dev.jgregorio.demo.vibe.coding.domain.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jgregorio.demo.vibe.coding.domain.exception.DomainValidationException;
import dev.jgregorio.demo.vibe.coding.domain.exception.ValidationError;
import dev.jgregorio.demo.vibe.coding.domain.model.Rocket;
import dev.jgregorio.demo.vibe.coding.domain.model.RocketRange;
import org.junit.jupiter.api.Test;

class RocketValidatorTest {

  @Test
  void validate_shouldPass_whenRocketIsValid() {
    // Given
    final Rocket rocket =
        Rocket.builder().name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();

    // When & Then
    RocketValidator.validate(rocket);
  }

  @Test
  void validate_shouldThrowDomainValidationException_whenNameIsEmpty() {
    // Given
    final Rocket rocket = Rocket.builder().name("").range(RocketRange.ORBITAL).capacity(5).build();

    // When & Then
    assertThatThrownBy(() -> RocketValidator.validate(rocket))
        .isInstanceOf(DomainValidationException.class)
        .satisfies(
            ex -> {
              final DomainValidationException validationEx = (DomainValidationException) ex;
              assertThat(validationEx.getErrors()).hasSize(1);
              final ValidationError error = validationEx.getErrors().get(0);
              assertThat(error.getDomain()).isEqualTo(RocketValidator.DOMAIN);
              assertThat(error.getField()).isEqualTo(RocketValidator.FIELD_NAME);
              assertThat(error.getCause()).isEqualTo(RocketValidator.CAUSE_REQUIRED);
            });
  }

  @Test
  void validate_shouldThrowDomainValidationException_whenNameIsNull() {
    // Given
    final Rocket rocket =
        Rocket.builder().name(null).range(RocketRange.ORBITAL).capacity(5).build();

    // When & Then
    assertThatThrownBy(() -> RocketValidator.validate(rocket))
        .isInstanceOf(DomainValidationException.class)
        .satisfies(
            ex -> {
              final DomainValidationException validationEx = (DomainValidationException) ex;
              assertThat(validationEx.getErrors()).hasSize(1);
              final ValidationError error = validationEx.getErrors().get(0);
              assertThat(error.getDomain()).isEqualTo(RocketValidator.DOMAIN);
              assertThat(error.getField()).isEqualTo(RocketValidator.FIELD_NAME);
              assertThat(error.getCause()).isEqualTo(RocketValidator.CAUSE_REQUIRED);
            });
  }

  @Test
  void validate_shouldThrowDomainValidationException_whenRangeIsNull() {
    // Given
    final Rocket rocket = Rocket.builder().name("Falcon").range(null).capacity(5).build();

    // When & Then
    assertThatThrownBy(() -> RocketValidator.validate(rocket))
        .isInstanceOf(DomainValidationException.class)
        .satisfies(
            ex -> {
              final DomainValidationException validationEx = (DomainValidationException) ex;
              assertThat(validationEx.getErrors()).hasSize(1);
              final ValidationError error = validationEx.getErrors().get(0);
              assertThat(error.getDomain()).isEqualTo(RocketValidator.DOMAIN);
              assertThat(error.getField()).isEqualTo(RocketValidator.FIELD_RANGE);
              assertThat(error.getCause()).isEqualTo(RocketValidator.CAUSE_REQUIRED);
            });
  }

  @Test
  void validate_shouldThrowDomainValidationException_whenCapacityIsNull() {
    // Given
    final Rocket rocket =
        Rocket.builder().name("Falcon").range(RocketRange.ORBITAL).capacity(null).build();

    // When & Then
    assertThatThrownBy(() -> RocketValidator.validate(rocket))
        .isInstanceOf(DomainValidationException.class)
        .satisfies(
            ex -> {
              final DomainValidationException validationEx = (DomainValidationException) ex;
              assertThat(validationEx.getErrors()).hasSize(1);
              final ValidationError error = validationEx.getErrors().get(0);
              assertThat(error.getDomain()).isEqualTo(RocketValidator.DOMAIN);
              assertThat(error.getField()).isEqualTo(RocketValidator.FIELD_CAPACITY);
              assertThat(error.getCause()).isEqualTo(RocketValidator.CAUSE_REQUIRED);
            });
  }

  @Test
  void validate_shouldThrowDomainValidationException_whenCapacityIsLessThanOne() {
    // Given
    final Rocket rocket =
        Rocket.builder().name("Falcon").range(RocketRange.ORBITAL).capacity(0).build();

    // When & Then
    assertThatThrownBy(() -> RocketValidator.validate(rocket))
        .isInstanceOf(DomainValidationException.class)
        .satisfies(
            ex -> {
              final DomainValidationException validationEx = (DomainValidationException) ex;
              assertThat(validationEx.getErrors()).hasSize(1);
              final ValidationError error = validationEx.getErrors().get(0);
              assertThat(error.getDomain()).isEqualTo(RocketValidator.DOMAIN);
              assertThat(error.getField()).isEqualTo(RocketValidator.FIELD_CAPACITY);
              assertThat(error.getCause()).isEqualTo(RocketValidator.CAUSE_INVALID);
            });
  }

  @Test
  void validate_shouldThrowDomainValidationException_whenCapacityIsGreaterThanTen() {
    // Given
    final Rocket rocket =
        Rocket.builder().name("Falcon").range(RocketRange.ORBITAL).capacity(11).build();

    // When & Then
    assertThatThrownBy(() -> RocketValidator.validate(rocket))
        .isInstanceOf(DomainValidationException.class)
        .satisfies(
            ex -> {
              final DomainValidationException validationEx = (DomainValidationException) ex;
              assertThat(validationEx.getErrors()).hasSize(1);
              final ValidationError error = validationEx.getErrors().get(0);
              assertThat(error.getDomain()).isEqualTo(RocketValidator.DOMAIN);
              assertThat(error.getField()).isEqualTo(RocketValidator.FIELD_CAPACITY);
              assertThat(error.getCause()).isEqualTo(RocketValidator.CAUSE_INVALID);
            });
  }

  @Test
  void validate_shouldThrowDomainValidationException_whenMultipleFieldsAreInvalid() {
    // Given
    final Rocket rocket = Rocket.builder().name("").range(null).capacity(15).build();

    // When & Then
    assertThatThrownBy(() -> RocketValidator.validate(rocket))
        .isInstanceOf(DomainValidationException.class)
        .satisfies(
            ex -> {
              final DomainValidationException validationEx = (DomainValidationException) ex;
              assertThat(validationEx.getErrors()).hasSize(3);
            });
  }
}
