package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.out.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jgregorio.demo.vibe.coding.domain.model.RocketRange;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

class RocketEntityIT extends BaseEntityTest {

  @Autowired private RocketRepository repository;

  @Test
  void create_shouldReturnRocket_whenSuccess() {
    // Given
    final RocketEntity toPersist = new RocketEntity();
    toPersist.setName("Falcon");
    toPersist.setRange(RocketRange.ORBITAL);
    toPersist.setCapacity(5);

    // When
    final RocketEntity persisted = getEntityManager().persist(toPersist);

    // Then
    assertThat(persisted).isNotNull();
    assertThat(persisted.getId()).isNotNull();
    assertThat(persisted.getName()).isEqualTo("Falcon");
    assertThat(persisted.getRange()).isEqualTo(RocketRange.ORBITAL);
    assertThat(persisted.getCapacity()).isEqualTo(5);
  }

  @Test
  void findByName_shouldReturnEntity_whenNameExists() {
    // Given
    final RocketEntity entity = new RocketEntity();
    entity.setName("Starship");
    entity.setRange(RocketRange.INTERPLANETARY);
    entity.setCapacity(10);
    getEntityManager().persist(entity);

    // When
    final Optional<RocketEntity> result = repository.findByName("Starship");

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().getName()).isEqualTo("Starship");
    assertThat(result.get().getRange()).isEqualTo(RocketRange.INTERPLANETARY);
    assertThat(result.get().getCapacity()).isEqualTo(10);
  }

  @Test
  void findByName_shouldReturnEmpty_whenNameDoesNotExist() {
    // Given & When
    final Optional<RocketEntity> result = repository.findByName("NonExistent");

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  void save_shouldThrowDataIntegrityViolationException_whenDuplicateName() {
    // Given
    final RocketEntity first = new RocketEntity();
    first.setName("Falcon");
    first.setRange(RocketRange.ORBITAL);
    first.setCapacity(5);
    getEntityManager().persist(first);

    final RocketEntity second = new RocketEntity();
    second.setName("Falcon");
    second.setRange(RocketRange.SUBORBITAL);
    second.setCapacity(3);

    // When & Then
    assertThatThrownBy(
            () -> {
              repository.saveAndFlush(second);
            })
        .isInstanceOf(DataIntegrityViolationException.class);
  }
}
