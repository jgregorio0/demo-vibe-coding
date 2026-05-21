package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.out.db;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jgregorio.demo.vibe.coding.domain.model.Rocket;
import dev.jgregorio.demo.vibe.coding.domain.model.RocketRange;
import org.junit.jupiter.api.Test;

class RocketPersistenceMapperTest {

  private final RocketPersistenceMapper mapper = new RocketPersistenceMapperImpl();

  @Test
  void toDomain_shouldMapEntityToDomain_whenInputIsValid() {
    // Given
    final RocketEntity entity = new RocketEntity();
    entity.setId(1L);
    entity.setName("Falcon");
    entity.setRange(RocketRange.ORBITAL);
    entity.setCapacity(5);

    // When
    final Rocket result = mapper.toDomain(entity);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getName()).isEqualTo("Falcon");
    assertThat(result.getRange()).isEqualTo(RocketRange.ORBITAL);
    assertThat(result.getCapacity()).isEqualTo(5);
  }

  @Test
  void toEntity_shouldMapDomainToEntity_whenInputIsValid() {
    // Given
    final Rocket domain =
        Rocket.builder().id(1L).name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();

    // When
    final RocketEntity result = mapper.toEntity(domain);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getName()).isEqualTo("Falcon");
    assertThat(result.getRange()).isEqualTo(RocketRange.ORBITAL);
    assertThat(result.getCapacity()).isEqualTo(5);
  }
}
