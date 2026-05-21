package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.out.db;

import dev.jgregorio.demo.vibe.coding.application.port.out.RocketPersistencePort;
import dev.jgregorio.demo.vibe.coding.domain.model.Rocket;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RocketPersistenceAdapter implements RocketPersistencePort {

  private final RocketRepository repository;
  private final RocketPersistenceMapper mapper;

  @Override
  public Rocket save(final Rocket rocket) {
    final RocketEntity entity = mapper.toEntity(rocket);
    final RocketEntity savedEntity = repository.save(entity);
    return mapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Rocket> findById(final Long id) {
    return repository.findById(id).map(mapper::toDomain);
  }

  @Override
  public Optional<Rocket> findByName(final String name) {
    return repository.findByName(name).map(mapper::toDomain);
  }

  @Override
  public List<Rocket> findAll() {
    return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
  }

  @Override
  public void deleteById(final Long id) {
    repository.deleteById(id);
  }

  @Override
  public boolean existsById(final Long id) {
    return repository.existsById(id);
  }
}
