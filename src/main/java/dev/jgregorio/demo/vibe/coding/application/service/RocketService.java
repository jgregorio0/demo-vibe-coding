package dev.jgregorio.demo.vibe.coding.application.service;

import dev.jgregorio.demo.vibe.coding.application.port.in.ManageRocketUseCase;
import dev.jgregorio.demo.vibe.coding.application.port.out.RocketPersistencePort;
import dev.jgregorio.demo.vibe.coding.domain.exception.AlreadyExistsException;
import dev.jgregorio.demo.vibe.coding.domain.exception.NotFoundException;
import dev.jgregorio.demo.vibe.coding.domain.model.Rocket;
import dev.jgregorio.demo.vibe.coding.domain.validator.RocketValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RocketService implements ManageRocketUseCase {

  private final RocketPersistencePort persistencePort;

  @Override
  public Rocket registerRocket(final Rocket rocket) {
    RocketValidator.validate(rocket);
    final Optional<Rocket> existing = persistencePort.findByName(rocket.getName());
    if (existing.isPresent()) {
      throw new AlreadyExistsException(
          String.format("Rocket with name '%s' already exists", rocket.getName()));
    }
    return persistencePort.save(rocket);
  }

  @Override
  public Rocket retrieveRocket(final Long id) {
    return persistencePort.findById(id)
        .orElseThrow(() -> new NotFoundException(
            String.format("Rocket with ID %d not found", id)));
  }

  @Override
  public List<Rocket> listRockets() {
    return persistencePort.findAll();
  }

  @Override
  public Rocket updateRocket(final Long id, final Rocket rocket) {
    final Rocket existing = retrieveRocket(id);

    final Rocket updatedRocket = Rocket.builder()
        .id(existing.getId())
        .name(rocket.getName())
        .range(rocket.getRange())
        .capacity(rocket.getCapacity())
        .build();

    RocketValidator.validate(updatedRocket);

    final Optional<Rocket> nameConflict = persistencePort.findByName(rocket.getName());
    final boolean isNameTakenByAnother = nameConflict.isPresent()
        && !Objects.equals(nameConflict.get().getId(), id);
    if (isNameTakenByAnother) {
      throw new AlreadyExistsException(
          String.format("Rocket with name '%s' already exists", rocket.getName()));
    }

    return persistencePort.save(updatedRocket);
  }

  @Override
  public void deleteRocket(final Long id) {
    if (!persistencePort.existsById(id)) {
      throw new NotFoundException(String.format("Rocket with ID %d not found", id));
    }
    persistencePort.deleteById(id);
  }
}
