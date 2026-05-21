---
trigger: glob
globs: "*PersistenceMapper.java"
---

## MapStruct Persistence Mapper Rules
- Target framework: MapStruct integration with Spring.
- Objective: Mapping between domain models and persistence entities.
- Define mapper interfaces with `@Mapper` annotation.
- Use `@Mapping` annotation for custom field mappings.
- Use `componentModel = MappingConstants.ComponentModel.SPRING` to allow Spring to inject mapper instances.
- Use `uses` to import other mappers.
- Mapper should have as suffix `PersistenceMapper`.
- Mapper should extend interface `GenericPersistenceMapper<{DomainModel}, {PersistenceEntity}>`.
- Should be located in `src/main/java/dev/jgregorio/demo/vibe/coding/infrastructure/adapter/out/db`.

```java
package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.out.db;

public interface GenericPersistenceMapper<D, E> {
  D toDomain(final E entity);

  E toEntity(final D domain);
}
```
