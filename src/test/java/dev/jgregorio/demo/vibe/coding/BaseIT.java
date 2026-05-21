package dev.jgregorio.demo.vibe.coding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public abstract class BaseIT {

  @Autowired private TestRestTemplate restTemplate;

  protected TestRestTemplate getRestTemplate() {
    return this.restTemplate;
  }
}
