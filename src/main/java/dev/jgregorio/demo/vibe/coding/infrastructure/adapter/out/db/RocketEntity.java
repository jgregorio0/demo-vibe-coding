package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.out.db;

import dev.jgregorio.demo.vibe.coding.domain.model.RocketRange;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rockets")
@Getter
@Setter
public class RocketEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RocketRange range;

  @Column(nullable = false)
  private Integer capacity;
}
