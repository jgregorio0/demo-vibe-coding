package dev.jgregorio.demo.vibe.coding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("dev")
public abstract class BaseIT {

  @Autowired private TestRestTemplate restTemplate;

  protected TestRestTemplate getRestTemplate() {
    return this.restTemplate;
  }
}
