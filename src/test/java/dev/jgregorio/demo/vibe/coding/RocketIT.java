package dev.jgregorio.demo.vibe.coding;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jgregorio.demo.vibe.coding.infrastructure.adapter.in.web.RocketRequest;
import dev.jgregorio.demo.vibe.coding.infrastructure.adapter.in.web.RocketResponse;
import dev.jgregorio.demo.vibe.coding.infrastructure.config.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

@Sql(
    scripts = "/rocket/rockets_initial.sql",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/rocket/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class RocketIT extends BaseIT {

  private static final String ENDPOINT = "/api/v1/rockets";

  @Test
  void registerRocket_shouldCreateRocketAndReturn201_whenRequestIsValid() {
    // Given
    final RocketRequest request =
        RocketRequest.builder().name("Falcon 9").range("ORBITAL").capacity(7).build();

    // When
    final ResponseEntity<RocketResponse> response =
        getRestTemplate().postForEntity(ENDPOINT, request, RocketResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    final RocketResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getId()).isNotNull();
    assertThat(body.getName()).isEqualTo("Falcon 9");
    assertThat(body.getRange()).isEqualTo("ORBITAL");
    assertThat(body.getCapacity()).isEqualTo(7);
  }

  @Test
  void registerRocket_shouldReturn409Conflict_whenNameAlreadyExists() {
    // Given
    final RocketRequest request =
        RocketRequest.builder().name("Falcon").range("ORBITAL").capacity(5).build();

    // When
    final ResponseEntity<ErrorResponse> response =
        getRestTemplate().postForEntity(ENDPOINT, request, ErrorResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    final ErrorResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getMessage()).contains("Rocket with name 'Falcon' already exists");
  }

  @Test
  void registerRocket_shouldReturn400BadRequest_whenCapacityIsInvalid() {
    // Given
    final RocketRequest request =
        RocketRequest.builder().name("Falcon 9").range("ORBITAL").capacity(0).build();

    // When
    final ResponseEntity<ErrorResponse> response =
        getRestTemplate().postForEntity(ENDPOINT, request, ErrorResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    final ErrorResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getErrors()).hasSize(1);
    assertThat(body.getErrors().get(0).getField()).isEqualTo("capacity");
  }

  @Test
  void registerRocket_shouldReturn400BadRequest_whenRangeIsInvalid() {
    // Given
    final RocketRequest request =
        RocketRequest.builder().name("Falcon 9").range("INVALID_RANGE").capacity(5).build();

    // When
    final ResponseEntity<ErrorResponse> response =
        getRestTemplate().postForEntity(ENDPOINT, request, ErrorResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    final ErrorResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getErrors()).hasSize(1);
    assertThat(body.getErrors().get(0).getField()).isEqualTo("range");
  }

  @Test
  void retrieveRocket_shouldReturn200Ok_whenRocketExists() {
    // Given
    final long id = 100L;

    // When
    final ResponseEntity<RocketResponse> response =
        getRestTemplate().getForEntity(ENDPOINT + "/" + id, RocketResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    final RocketResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getId()).isEqualTo(id);
    assertThat(body.getName()).isEqualTo("Falcon");
    assertThat(body.getRange()).isEqualTo("ORBITAL");
    assertThat(body.getCapacity()).isEqualTo(5);
  }

  @Test
  void retrieveRocket_shouldReturn404NotFound_whenRocketDoesNotExist() {
    // Given
    final long id = 999L;

    // When
    final ResponseEntity<ErrorResponse> response =
        getRestTemplate().getForEntity(ENDPOINT + "/" + id, ErrorResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    final ErrorResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getMessage()).contains("Rocket with ID 999 not found");
  }

  @Test
  void updateRocket_shouldUpdateRocketAndReturn200Ok_whenRequestIsValid() {
    // Given
    final long id = 100L;
    final RocketRequest request =
        RocketRequest.builder().name("Falcon 9 v1.2").range("ORBITAL").capacity(9).build();

    // When
    final ResponseEntity<RocketResponse> response =
        getRestTemplate()
            .exchange(
                ENDPOINT + "/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                RocketResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    final RocketResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getId()).isEqualTo(id);
    assertThat(body.getName()).isEqualTo("Falcon 9 v1.2");
    assertThat(body.getRange()).isEqualTo("ORBITAL");
    assertThat(body.getCapacity()).isEqualTo(9);
  }

  @Test
  void updateRocket_shouldReturn404NotFound_whenRocketDoesNotExist() {
    // Given
    final long id = 999L;
    final RocketRequest request =
        RocketRequest.builder().name("Falcon 9 v1.2").range("ORBITAL").capacity(9).build();

    // When
    final ResponseEntity<ErrorResponse> response =
        getRestTemplate()
            .exchange(
                ENDPOINT + "/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                ErrorResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void deleteRocket_shouldReturn204NoContent_whenRocketExists() {
    // Given
    final long id = 100L;

    // When
    final ResponseEntity<Void> response =
        getRestTemplate().exchange(ENDPOINT + "/" + id, HttpMethod.DELETE, null, Void.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    final ResponseEntity<ErrorResponse> checkResponse =
        getRestTemplate().getForEntity(ENDPOINT + "/" + id, ErrorResponse.class);
    assertThat(checkResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void deleteRocket_shouldReturn404NotFound_whenRocketDoesNotExist() {
    // Given
    final long id = 999L;

    // When
    final ResponseEntity<ErrorResponse> response =
        getRestTemplate()
            .exchange(ENDPOINT + "/" + id, HttpMethod.DELETE, null, ErrorResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void listRockets_shouldReturnAllRocketsAnd200Ok_whenRocketsExist() {
    // Given & When
    final ResponseEntity<RocketResponse[]> response =
        getRestTemplate().getForEntity(ENDPOINT, RocketResponse[].class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    final RocketResponse[] body = response.getBody();
    assertThat(body).isNotNull().hasSize(2);
    assertThat(body[0].getName()).isEqualTo("Falcon");
    assertThat(body[1].getName()).isEqualTo("Falcon Heavy");
  }
}
