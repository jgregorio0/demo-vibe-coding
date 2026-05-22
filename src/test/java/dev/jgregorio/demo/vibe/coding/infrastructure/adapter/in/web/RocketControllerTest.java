package dev.jgregorio.demo.vibe.coding.infrastructure.adapter.in.web;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.jgregorio.demo.vibe.coding.application.port.in.ManageRocketUseCase;
import dev.jgregorio.demo.vibe.coding.domain.exception.AlreadyExistsException;
import dev.jgregorio.demo.vibe.coding.domain.exception.NotFoundException;
import dev.jgregorio.demo.vibe.coding.domain.model.Rocket;
import dev.jgregorio.demo.vibe.coding.domain.model.RocketRange;
import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(RocketController.class)
@AutoConfigureMockMvc(addFilters = false)
class RocketControllerTest extends BaseControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private JsonMapper objectMapper;

  @MockitoBean private ManageRocketUseCase useCase;

  @MockitoBean private RocketApiMapper mapper;

  @Test
  void registerRocket_shouldReturn201Created_whenRequestIsValid() throws Exception {
    // Given
    final RocketRequest request =
        RocketRequest.builder().name("Falcon").range("ORBITAL").capacity(5).build();
    final Rocket domainRocket =
        Rocket.builder().name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();
    final Rocket registeredRocket =
        Rocket.builder().id(1L).name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();
    final RocketResponse response =
        RocketResponse.builder().id(1L).name("Falcon").range("ORBITAL").capacity(5).build();

    when(mapper.toDomain(any(RocketRequest.class))).thenReturn(domainRocket);
    when(useCase.registerRocket(any(Rocket.class))).thenReturn(registeredRocket);
    when(mapper.toResponse(any(Rocket.class))).thenReturn(response);

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/rockets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Falcon")))
        .andExpect(jsonPath("$.range", is("ORBITAL")))
        .andExpect(jsonPath("$.capacity", is(5)));
  }

  @Test
  void registerRocket_shouldReturn400BadRequest_whenRequestBodyIsInvalid() throws Exception {
    // Given
    final RocketRequest request =
        RocketRequest.builder().name("").range("ORBITAL").capacity(5).build();

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/rockets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void registerRocket_shouldReturn409Conflict_whenRocketNameExists() throws Exception {
    // Given
    final RocketRequest request =
        RocketRequest.builder().name("Falcon").range("ORBITAL").capacity(5).build();
    final Rocket domainRocket =
        Rocket.builder().name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();

    when(mapper.toDomain(any(RocketRequest.class))).thenReturn(domainRocket);
    when(useCase.registerRocket(any(Rocket.class)))
        .thenThrow(new AlreadyExistsException("Conflict"));

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/rockets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isConflict());
  }

  @Test
  void retrieveRocket_shouldReturn200Ok_whenRocketExists() throws Exception {
    // Given
    final Rocket domainRocket =
        Rocket.builder().id(1L).name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();
    final RocketResponse response =
        RocketResponse.builder().id(1L).name("Falcon").range("ORBITAL").capacity(5).build();

    when(useCase.retrieveRocket(1L)).thenReturn(domainRocket);
    when(mapper.toResponse(domainRocket)).thenReturn(response);

    // When & Then
    mockMvc
        .perform(get("/api/v1/rockets/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Falcon")))
        .andExpect(jsonPath("$.range", is("ORBITAL")))
        .andExpect(jsonPath("$.capacity", is(5)));
  }

  @Test
  void retrieveRocket_shouldReturn404NotFound_whenRocketDoesNotExist() throws Exception {
    // Given
    when(useCase.retrieveRocket(99L)).thenThrow(new NotFoundException("Not Found"));

    // When & Then
    mockMvc.perform(get("/api/v1/rockets/{id}", 99L)).andExpect(status().isNotFound());
  }

  @Test
  void listRockets_shouldReturn200Ok_whenRocketsExist() throws Exception {
    // Given
    final Rocket domainRocket =
        Rocket.builder().id(1L).name("Falcon").range(RocketRange.ORBITAL).capacity(5).build();
    final RocketResponse response =
        RocketResponse.builder().id(1L).name("Falcon").range("ORBITAL").capacity(5).build();

    when(useCase.listRockets()).thenReturn(List.of(domainRocket));
    when(mapper.toResponse(domainRocket)).thenReturn(response);

    // When & Then
    mockMvc
        .perform(get("/api/v1/rockets"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].name", is("Falcon")));
  }

  @Test
  void updateRocket_shouldReturn200Ok_whenRequestIsValidAndRocketExists() throws Exception {
    // Given
    final RocketRequest request =
        RocketRequest.builder().name("Falcon Heavy").range("INTERPLANETARY").capacity(10).build();
    final Rocket domainRocket =
        Rocket.builder()
            .name("Falcon Heavy")
            .range(RocketRange.INTERPLANETARY)
            .capacity(10)
            .build();
    final Rocket updatedRocket =
        Rocket.builder()
            .id(1L)
            .name("Falcon Heavy")
            .range(RocketRange.INTERPLANETARY)
            .capacity(10)
            .build();
    final RocketResponse response =
        RocketResponse.builder()
            .id(1L)
            .name("Falcon Heavy")
            .range("INTERPLANETARY")
            .capacity(10)
            .build();

    when(mapper.toDomain(any(RocketRequest.class))).thenReturn(domainRocket);
    when(useCase.updateRocket(eq(1L), any(Rocket.class))).thenReturn(updatedRocket);
    when(mapper.toResponse(any(Rocket.class))).thenReturn(response);

    // When & Then
    mockMvc
        .perform(
            put("/api/v1/rockets/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Falcon Heavy")))
        .andExpect(jsonPath("$.range", is("INTERPLANETARY")))
        .andExpect(jsonPath("$.capacity", is(10)));
  }

  @Test
  void updateRocket_shouldReturn404NotFound_whenRocketDoesNotExist() throws Exception {
    // Given
    final RocketRequest request =
        RocketRequest.builder().name("Falcon Heavy").range("INTERPLANETARY").capacity(10).build();
    final Rocket domainRocket =
        Rocket.builder()
            .name("Falcon Heavy")
            .range(RocketRange.INTERPLANETARY)
            .capacity(10)
            .build();

    when(mapper.toDomain(any(RocketRequest.class))).thenReturn(domainRocket);
    when(useCase.updateRocket(eq(99L), any(Rocket.class)))
        .thenThrow(new NotFoundException("Not Found"));

    // When & Then
    mockMvc
        .perform(
            put("/api/v1/rockets/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteRocket_shouldReturn204NoContent_whenRocketExists() throws Exception {
    // Given & When & Then
    mockMvc.perform(delete("/api/v1/rockets/{id}", 1L)).andExpect(status().isNoContent());
  }

  @Test
  void deleteRocket_shouldReturn404NotFound_whenRocketDoesNotExist() throws Exception {
    // Given
    doThrow(new NotFoundException("Not Found")).when(useCase).deleteRocket(99L);

    // When & Then
    mockMvc.perform(delete("/api/v1/rockets/{id}", 99L)).andExpect(status().isNotFound());
  }
}
