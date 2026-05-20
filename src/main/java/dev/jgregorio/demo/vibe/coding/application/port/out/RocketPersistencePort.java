package dev.jgregorio.demo.vibe.coding.application.port.out;

import dev.jgregorio.demo.vibe.coding.domain.model.Rocket;
import java.util.List;
import java.util.Optional;

public interface RocketPersistencePort {
  Rocket save(final Rocket rocket);
  Optional<Rocket> findById(final Long id);
  Optional<Rocket> findByName(final String name);
  List<Rocket> findAll();
  void deleteById(final Long id);
  boolean existsById(final Long id);
}
