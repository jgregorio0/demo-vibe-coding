package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.out.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("dev")
public abstract class BaseEntityTest {

  @Autowired private TestEntityManager entityManager;

  protected TestEntityManager getEntityManager() {
    return this.entityManager;
  }
}
