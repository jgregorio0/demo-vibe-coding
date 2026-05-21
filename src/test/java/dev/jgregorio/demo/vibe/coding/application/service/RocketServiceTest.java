package dev.jgregorio.demo.vibe.coding.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.jgregorio.demo.vibe.coding.application.port.out.RocketPersistencePort;
import dev.jgregorio.demo.vibe.coding.domain.exception.AlreadyExistsException;
import dev.jgregorio.demo.vibe.coding.domain.exception.DomainValidationException;
import dev.jgregorio.demo.vibe.coding.domain.exception.NotFoundException;
import dev.jgregorio.demo.vibe.coding.domain.model.Rocket;
import dev.jgregorio.demo.vibe.coding.domain.model.RocketRange;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RocketServiceTest {

  @Mock private RocketPersistencePort persistencePort;

  @InjectMocks private RocketService service;

  @Test
  void registerRocket_shouldSaveAndReturnRocket_whenRocketIsValidAndUniqueName() {
    // Given
    final Rocket rocket =
        Rocket.builder().name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();
    final Rocket savedRocket =
        Rocket.builder().id(1L).name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();
    when(persistencePort.findByName("Falcon")).thenReturn(Optional.empty());
    when(persistencePort.save(any(Rocket.class))).thenReturn(savedRocket);

    // When
    final Rocket result = service.registerRocket(rocket);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getName()).isEqualTo("Falcon");
    assertThat(result.getRange()).isEqualTo(RocketRange.ORBITAL);
    assertThat(result.getCapacity()).isEqualTo(5);
  }

  @Test
  void registerRocket_shouldThrowAlreadyExistsException_whenRocketNameExists() {
    // Given
    final Rocket rocket =
        Rocket.builder().name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();
    final Rocket existing =
        Rocket.builder().id(1L).name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();
    when(persistencePort.findByName("Falcon")).thenReturn(Optional.of(existing));

    // When & Then
    assertThatThrownBy(() -> service.registerRocket(rocket))
        .isInstanceOf(AlreadyExistsException.class)
        .hasMessageContaining("Rocket with name 'Falcon' already exists");
  }

  @Test
  void registerRocket_shouldThrowDomainValidationException_whenRocketIsInvalid() {
    // Given
    final Rocket rocket = Rocket.builder().name("").range(RocketRange.ORBITAL).capacity(5).build();

    // When & Then
    assertThatThrownBy(() -> service.registerRocket(rocket))
        .isInstanceOf(DomainValidationException.class);
  }

  @Test
  void retrieveRocket_shouldReturnRocket_whenRocketExists() {
    // Given
    final Rocket rocket =
        Rocket.builder().id(1L).name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();
    when(persistencePort.findById(1L)).thenReturn(Optional.of(rocket));

    // When
    final Rocket result = service.retrieveRocket(1L);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getName()).isEqualTo("Falcon");
  }

  @Test
  void retrieveRocket_shouldThrowNotFoundException_whenRocketDoesNotExist() {
    // Given
    when(persistencePort.findById(99L)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> service.retrieveRocket(99L))
        .isInstanceOf(NotFoundException.class)
        .hasMessageContaining("Rocket with ID 99 not found");
  }

  @Test
  void listRockets_shouldReturnAllRockets_whenRocketsExist() {
    // Given
    final Rocket rocket =
        Rocket.builder().id(1L).name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();
    when(persistencePort.findAll()).thenReturn(List.of(rocket));

    // When
    final List<Rocket> result = service.listRockets();

    // Then
    assertThat(result).isNotNull().hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("Falcon");
  }

  @Test
  void listRockets_shouldReturnEmptyList_whenNoRocketsExist() {
    // Given
    when(persistencePort.findAll()).thenReturn(Collections.emptyList());

    // When
    final List<Rocket> result = service.listRockets();

    // Then
    assertThat(result).isNotNull().isEmpty();
  }

  @Test
  void updateRocket_shouldUpdateAndReturnRocket_whenRocketExistsAndDataIsValid() {
    // Given
    final Rocket existing =
        Rocket.builder().id(1L).name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();
    final Rocket updateInfo =
        Rocket.builder()
            .name("Falcon Heavy")
            .range(RocketRange.INTERPLANETARY)
            .capacity(10)
            .build();
    final Rocket updated =
        Rocket.builder()
            .id(1L)
            .name("Falcon Heavy")
            .range(RocketRange.INTERPLANETARY)
            .capacity(10)
            .build();
    when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));
    when(persistencePort.findByName("Falcon Heavy")).thenReturn(Optional.empty());
    when(persistencePort.save(any(Rocket.class))).thenReturn(updated);

    // When
    final Rocket result = service.updateRocket(1L, updateInfo);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getName()).isEqualTo("Falcon Heavy");
    assertThat(result.getRange()).isEqualTo(RocketRange.INTERPLANETARY);
    assertThat(result.getCapacity()).isEqualTo(10);
  }

  @Test
  void updateRocket_shouldThrowNotFoundException_whenRocketDoesNotExist() {
    // Given
    final Rocket updateInfo =
        Rocket.builder()
            .name("Falcon Heavy")
            .range(RocketRange.INTERPLANETARY)
            .capacity(10)
            .build();
    when(persistencePort.findById(99L)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> service.updateRocket(99L, updateInfo))
        .isInstanceOf(NotFoundException.class);
  }

  @Test
  void updateRocket_shouldThrowAlreadyExistsException_whenRocketNameExistsForAnotherId() {
    // Given
    final Rocket existing =
        Rocket.builder().id(1L).name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();
    final Rocket anotherRocket =
        Rocket.builder()
            .id(2L)
            .name("Starship")
            .range(RocketRange.INTERPLANETARY)
            .capacity(10)
            .build();
    final Rocket updateInfo =
        Rocket.builder().name("Starship").range(RocketRange.INTERPLANETARY).capacity(10).build();
    when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));
    when(persistencePort.findByName("Starship")).thenReturn(Optional.of(anotherRocket));

    // When & Then
    assertThatThrownBy(() -> service.updateRocket(1L, updateInfo))
        .isInstanceOf(AlreadyExistsException.class)
        .hasMessageContaining("Rocket with name 'Starship' already exists");
  }

  @Test
  void updateRocket_shouldThrowDomainValidationException_whenUpdatedRocketIsInvalid() {
    // Given
    final Rocket existing =
        Rocket.builder().id(1L).name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();
    final Rocket updateInfo =
        Rocket.builder().name("").range(RocketRange.INTERPLANETARY).capacity(10).build();
    when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));

    // When & Then
    assertThatThrownBy(() -> service.updateRocket(1L, updateInfo))
        .isInstanceOf(DomainValidationException.class);
  }

  @Test
  void deleteRocket_shouldDeleteRocket_whenRocketExists() {
    // Given
    when(persistencePort.existsById(1L)).thenReturn(true);

    // When
    service.deleteRocket(1L);

    // Then
    verify(persistencePort).deleteById(1L);
  }

  @Test
  void deleteRocket_shouldThrowNotFoundException_whenRocketDoesNotExist() {
    // Given
    when(persistencePort.existsById(99L)).thenReturn(false);

    // When & Then
    assertThatThrownBy(() -> service.deleteRocket(99L)).isInstanceOf(NotFoundException.class);
  }
}
