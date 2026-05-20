package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.out.db;

import dev.jgregorio.demo.vibe.coding.domain.model.Rocket;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RocketPersistenceMapper extends GenericPersistenceMapper<Rocket, RocketEntity> {

  @Override
  Rocket toDomain(final RocketEntity entity);

  @Override
  RocketEntity toEntity(final Rocket domain);
}
