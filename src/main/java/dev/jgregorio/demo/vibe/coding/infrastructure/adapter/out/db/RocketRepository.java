package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.out.db;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RocketRepository extends JpaRepository<RocketEntity, Long> {
  Optional<RocketEntity> findByName(final String name);
}
