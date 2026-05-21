---
trigger: glob
globs: "*WebMapper.java"
---

## MapStruct Persistence Mapper Rules
- Target framework: MapStruct integration with Spring.
- Objective: Mapping between domain models and web request and responses.
- Define mapper interfaces with `@Mapper` annotation.
- Use `@Mapping` annotation for custom field mappings.
- Use `componentModel = MappingConstants.ComponentModel.SPRING` to allow Spring to inject mapper instances.
- Use `uses` to import other mappers.
- Mapper should have as suffix `WebMapper`.
- Mapper should extend interface `GenericWebMapper<{DomainModel}, {PersistenceEntity}>`.
- Should be located in `src/main/java/dev/jgregorio/demo/vibe/coding/infrastructure/adapter/in/web`.
```java
package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.out.db;

public interface GenericPersistenceMapper<D, E> {
  D toDomain(final E entity);

  E toEntity(final D domain);
}
```

- *Example:*
```java
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AssociatedPersistenceMapper.class})
public interface ExamplePersistenceMapper extends GenericPersistenceMapper<Domain, Entity> {

    @Override
    @Mapping(target = "field", source = "entityField")
    Domain toDomain(Entity entity);
}
```
