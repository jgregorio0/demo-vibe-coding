package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.in.web;

import dev.jgregorio.demo.vibe.coding.application.port.in.ManageRocketUseCase;
import dev.jgregorio.demo.vibe.coding.domain.model.Rocket;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/rockets")
@RequiredArgsConstructor
public class RocketController {

  private final ManageRocketUseCase useCase;
  private final RocketApiMapper mapper;

  @PostMapping
  public ResponseEntity<RocketResponse> registerRocket(@Valid @RequestBody final RocketRequest request) {
    final Rocket rocket = mapper.toDomain(request);
    final Rocket registered = useCase.registerRocket(rocket);
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(registered));
  }

  @GetMapping("/{id}")
  public ResponseEntity<RocketResponse> retrieveRocket(@PathVariable final Long id) {
    final Rocket rocket = useCase.retrieveRocket(id);
    return ResponseEntity.ok(mapper.toResponse(rocket));
  }

  @GetMapping
  public ResponseEntity<List<RocketResponse>> listRockets() {
    final List<RocketResponse> responseList = useCase.listRockets().stream()
        .map(mapper::toResponse)
        .collect(Collectors.toList());
    return ResponseEntity.ok(responseList);
  }

  @PutMapping("/{id}")
  public ResponseEntity<RocketResponse> updateRocket(
      @PathVariable final Long id,
      @Valid @RequestBody final RocketRequest request) {
    final Rocket rocket = mapper.toDomain(request);
    final Rocket updated = useCase.updateRocket(id, rocket);
    return ResponseEntity.ok(mapper.toResponse(updated));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRocket(@PathVariable final Long id) {
    useCase.deleteRocket(id);
    return ResponseEntity.noContent().build();
  }
}
