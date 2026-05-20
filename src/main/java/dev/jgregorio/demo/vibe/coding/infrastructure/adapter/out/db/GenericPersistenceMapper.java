package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.out.db;

public interface GenericPersistenceMapper<D, E> {
  D toDomain(final E entity);
  E toEntity(final D domain);
}
