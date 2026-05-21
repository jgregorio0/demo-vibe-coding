package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.in.web;

import dev.jgregorio.demo.vibe.coding.domain.exception.DomainValidationException;
import dev.jgregorio.demo.vibe.coding.domain.exception.ValidationError;
import dev.jgregorio.demo.vibe.coding.domain.model.Rocket;
import dev.jgregorio.demo.vibe.coding.domain.model.RocketRange;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RocketApiMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "range", source = "range", qualifiedByName = "mapRange")
  Rocket toDomain(final RocketRequest request);

  RocketResponse toResponse(final Rocket domain);

  @Named("mapRange")
  default RocketRange mapRange(final String range) {
    if (range == null || range.trim().isEmpty()) {
      return null;
    }
    try {
      return RocketRange.valueOf(range.toUpperCase().trim());
    } catch (IllegalArgumentException e) {
      throw new DomainValidationException(
          List.of(
              ValidationError.builder().domain("rocket").field("range").cause("INVALID").build()));
    }
  }
}
