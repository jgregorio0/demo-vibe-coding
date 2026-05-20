package dev.jgregorio.demo.vibe.coding.application.port.in;

import dev.jgregorio.demo.vibe.coding.domain.model.Rocket;
import java.util.List;

public interface ManageRocketUseCase {
  Rocket registerRocket(final Rocket rocket);
  Rocket retrieveRocket(final Long id);
  List<Rocket> listRockets();
  Rocket updateRocket(final Long id, final Rocket rocket);
  void deleteRocket(final Long id);
}
