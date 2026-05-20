package dev.jgregorio.demo.vibe.coding.domain.validator;

import dev.jgregorio.demo.vibe.coding.domain.exception.DomainValidationException;
import dev.jgregorio.demo.vibe.coding.domain.exception.ValidationError;
import dev.jgregorio.demo.vibe.coding.domain.model.Rocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RocketValidator {

  public static final String DOMAIN = "rocket";
  public static final String FIELD_NAME = "name";
  public static final String FIELD_RANGE = "range";
  public static final String FIELD_CAPACITY = "capacity";

  public static final String CAUSE_REQUIRED = "REQUIRED";
  public static final String CAUSE_INVALID = "INVALID";

  public static String getDomain() {
    return DOMAIN;
  }

  public static void validate(final Rocket rocket) {
    final List<ValidationError> errors = new ArrayList<>();

    if (Objects.isNull(rocket.getName()) || rocket.getName().trim().isEmpty()) {
      errors.add(ValidationError.builder()
          .domain(DOMAIN)
          .field(FIELD_NAME)
          .cause(CAUSE_REQUIRED)
          .build());
    }

    if (Objects.isNull(rocket.getRange())) {
      errors.add(ValidationError.builder()
          .domain(DOMAIN)
          .field(FIELD_RANGE)
          .cause(CAUSE_REQUIRED)
          .build());
    }

    if (Objects.isNull(rocket.getCapacity())) {
      errors.add(ValidationError.builder()
          .domain(DOMAIN)
          .field(FIELD_CAPACITY)
          .cause(CAUSE_REQUIRED)
          .build());
    } else {
      final boolean isCapacityInvalid = rocket.getCapacity() < 1 || rocket.getCapacity() > 10;
      if (isCapacityInvalid) {
        errors.add(ValidationError.builder()
            .domain(DOMAIN)
            .field(FIELD_CAPACITY)
            .cause(CAUSE_INVALID)
            .build());
      }
    }

    final boolean hasErrors = !errors.isEmpty();
    if (hasErrors) {
      throw new DomainValidationException(errors);
    }
  }
}
