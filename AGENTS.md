## Architecture

Hexagonal Architecture (Ports and Adapters): The project implements the Hexagonal Architecture pattern to enforce a
clear separation of concerns between the core business logic (domain), ports (application) and external dependencies
adapters (infrastructure).

```text
src/
├── main/
│   ├── java/com/example/api/
│   │   ├── domain/          # Core business logic (isolated from frameworks)
│   │   │   ├── model/       # Domain entities and value objects
│   │   │   └── exception/   # Domain-specific business exceptions
│   │   ├── application/     # Application use cases and Ports
│   │   │   ├── port/        # Interfaces defining inputs and outputs
│   │   │   │   ├── in/      # Primary/Driving ports (Use cases)
│   │   │   │   └── out/     # Secondary/Driven ports (Data access, external APIs)
│   │   │   └── service/     # Implementations of primary ports (Use case logic)
│   │   └── infrastructure/  # Frameworks, configurations, and Adapters
│   │       ├── adapter/     # Implementations of ports
│   │       │   ├── in/      # Driving adapters (REST Controllers, WebSockets, etc.)
│   │       │   │   └── web/ # Controllers, REST DTOs, request/response mappers
│   │       │   └── out/     # Driven adapters (Database, external services)
│   │       │       └── db/  # JPA Entities, Spring Data repositories, DB mappers
│   │       └── config/      # Spring configuration classes (Beans, Security, etc.)
│   └── resources/
│       ├── db/migration/    # Flyway Migration Scripts
│       ├── application.yml  # Main application configurations
│       └── application-dev.yml # Development environment overrides
└── test/                    # Unit and Integration Tests
```

## Core Framework

- **Language:** Java 25
- **Runtime framework:** Spring Boot 4
- **Build Tool:** Gradle
- **Database:** PostgreSQL (Production) & H2 (In-memory testing)
- **Persistence:** Spring Data JPA & Hibernate
- **Database Migrations:** Flyway
- **Security:** Spring Security (JWT-based authentication & authorization)
- **API Documentation:** OpenAPI 3 / Swagger UI
- **Testing:** JUnit 5, Mockito, Testcontainers (for database integration tests)
- **Code Quality:** Lombok, MapStruct (for DTO mapping)
- **Containerization:** Docker & Docker Compose
- **Monitoring:** Spring Boot Actuator & Prometheus

## Code Formatting

- [Code style guide](docs/guidelines/code-style.md)
- Use google-java-format.
- Use gradlew spotlessApply to format code.

## Code guidelines
- Use Hexagonal Architecture.
- Use Clean Code.
- Use SOLID principles.
- Use KISS principle.

## Coding Style

- Use UTF-8 encoding.
- Use descriptive names for classes, methods, and variables.
- Avoid `var` keyword, prefer explicit types.
- All method parameters should be `final`.
- All variables should be declared as `final` where possible.
- Preference for immutability:
  - Avoid mutations of objects, specially when using for-each loops or Stream API using `forEach()`.
  - Avoid magic numbers and strings; use constants instead.
  - Check emptiness and nullness before operations on collections and strings.
  - Avoid methods using `throws` clause; prefer unchecked exceptions.
- Avoid comments.
- Use `@Override` annotation when overriding methods.
- Preference for Objects.*isNull() and Objects.*nonNull() than direct null checks.
- Wrap multiple conditions in a boolean variable for better readability.
- Prefer early returns.
- Avoid else statements when not necessary and try early returns.
- Follow best practices and clean code.
- When overriding toString use String.format `String.format("Resource[id=%d]", id);`
- Avoid `@Autowired`. Prefer constructor injection.
- Use conventional Commits v1.0.0

## Annotations

- **`@Service`**: For business logic classes.
- **`@Repository`**: For data access classes that extend JPA repositories or interact with the database.
- **`@RestController`**: For web controllers.
- **`@Component`**: For generic Spring components.
- **`@Configuration`**: For Spring configuration classes.
- **`@ConfigurationProperties`**: For binding related properties avoid multiple `@Value` annotations. From more than 2
  properties, consider using this annotation.
- **`@Valid`**: To validate inputs in controllers.

## Lombok Annotations

- Use `@RequiredArgsConstructor` from Lombok for dependency injection via constructor.
- Use `@Slf4j` from Lombok for logging.
- Use `@Builder` for object creation.
- Avoid `@Data` annotation; prefer `@Getter` and `@Setter` for granular control.

## Mappers

**Use MapStruct**

- For mapping between DTOs and entities.
- Define mapper interfaces with `@Mapper` annotation.
- Use `@Mapping` annotation for custom field mappings.
- Use `componentModel = MappingConstants.ComponentModel.SPRING` to allow Spring to manage mapper instances.
- Use `uses` to import other mappers.
- Mapper should have as suffix `Mapper`.
- Mapper should implement interface `GenericPersistenceMapper`. Do not override update.
- Name mapper methods clearly: toDomain and toEntity.
- Example Mapper Interface:

```java

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {WorkerPersistenceMapper.class})
public interface ExecutionEnrollmentPersistenceMapper
        extends GenericPersistenceMapper<ExecutionEnrollment, ExecutionEnrollmentEntity> {

  @Override
  @Mapping(target = "state", constant = DEFAULT_STATE)
  @Mapping(target = "signatureId", source = "signature.id")
  @Mapping(target = "additionalPositions", ignore = true)
  ExecutionEnrollment toDomain(ExecutionEnrollmentEntity entity);

  @Override
  @Mapping(target = "executionId", source = "execution.id")
  @Mapping(target = "workerId", source = "worker.id")
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedDate", ignore = true)
  ExecutionEnrollmentEntity toEntity(ExecutionEnrollment domain);
}
```

- For testing mappers, add dependency to the mapper using `private final ExecutionEnrollmentPersistenceMapper mapper;`
  and
  `@RequiredArgsConstructor` annotation.

## Exception Handling

- Custom Exceptions: Create custom domain exception classes extending `RuntimeException`.
- Global Exception Handler: Use `@ControllerAdvice` and `@ExceptionHandler` to handle exceptions globally.
- HTTP Status Codes: Map exceptions to appropriate HTTP status codes in REST controllers.
- Error Response Structure: Define a consistent error response structure

## Testing

- **Black-Box Testing**: Prioritize testing system behavior and output over internal implementation details.
- **State-Based Assertions**: Verify the system reaches the correct state. Avoid interaction testing (e.g., avoid
  `verify(mock).method()`).
- **Realism over Isolation**: Minimize mocks. Build real objects and use in-memory databases whenever possible. Use the
  Builder Pattern for object creation instead of mocking data structures.
- **Libraries**:
  - Framework: **JUnit 5** (Jupiter). Do not use JUnit 4.
  - Mocking: **Mockito** (only when real objects are not feasible). Use `when().then()` to stub.
  - Assertions: **AssertJ**. Always use static imports for `assertThat` (
    `import static org.assertj.core.api.Assertions.assertThat;`).
- **Unit test**:
  - Use plain Java (no Spring context). Use `@Mock` and `@InjectMocks` for dependencies.
  - Test domain logic.
  - File path: `src/test/java/...`
  - Class ends with `Test`, e.g. `ExecutionTest`.

```java

class ExecutionTest {

  private static final Long EXECUTION_ID = 1L;

  @Test
  void fromId_shouldCreateExecutionWithOnlyId() {
    // When
    Execution execution = Execution.from(EXECUTION_ID);

    // Then
    assertThat(execution.id()).isEqualTo(EXECUTION_ID);
    assertThat(execution.type()).isNull();
    assertThat(execution.date()).isNull();
    assertThat(execution.course()).isNull();
  }
}
```

- **Presentation layer slice test**:
  - Use `@WebMvcTest(ControllerClass.class)`.
  - Use `@MockBean` to mock underlying services.
  - File path: `src/test/java/...`
  - Extends `BaseControllerTest`
  - Presentation layer slice test ends with `ControllerTest`, e.g. `ExecutionControllerTest`.

```java

@WebMvcTest(WorkerAttendanceController.class)
@AutoConfigureMockMvc(addFilters = false)
class SearchWorkerAttendanceControllerTest extends BaseControllerTest {

  public static final String ENDPOINT_URI = "/attendances/workers/search";

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private WorkerAttendanceService workerAttendanceService;
  @MockitoBean
  private WorkerAttendanceSearchApiMapper searchMapper;
  @MockitoBean
  private WorkerAttendanceCreationApiMapper creationMapper;
  @MockitoBean
  private WorkerAttendanceApiMapper mapper;
  @MockitoBean
  private WorkerAttendanceReportCreateApiMapper reportMapper;

  private static Stream<Arguments> provideInvalidRequests() {
    return Stream.of(
            Arguments.of(null, 20L, 30L), Arguments.of(10L, null, 30L), Arguments.of(10L, 20L, null));
  }

  @Test
  void search_shouldReturn200OkWithWorkerAttendances() throws Exception {
    // Given
    WorkerAttendanceSearchRequest request =
            WorkerAttendanceSearchRequest.builder()
                    .executionId(10L)
                    .clientId(20L)
                    .centerId(30L)
                    .build();
    WorkerAttendanceResponse response =
            WorkerAttendanceResponse.builder()
                    .id(1L)
                    .worker(WorkerTestFactory.buildWorkerResponseSample())
                    .type(WorkerAttendanceType.DEVICE)
                    .createdDate(LocalDateTime.of(2026, 2, 20, 13, 15))
                    .build();
    stubAttendancesWorkerSearch(response);
    // When & Then
    mockMvc
            .perform(
                    post(ENDPOINT_URI)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(1)))
            .andExpect(jsonPath("$.content[0].id", is(response.id().intValue())))
            .andExpect(jsonPath("$.content[0].worker.id", is(response.worker().id().intValue())))
            .andExpect(jsonPath("$.content[0].worker.code", is(response.worker().code())))
            .andExpect(jsonPath("$.content[0].worker.name", is(response.worker().name())))
            .andExpect(jsonPath("$.content[0].worker.surname", is(response.worker().surname())))
            .andExpect(jsonPath("$.content[0].type", is(response.type().name())))
            .andExpect(
                    jsonPath(
                            "$.content[0].createdDate",
                            is(dateTimeFormatterDmyHms.format(response.createdDate()))));
  }

  @ParameterizedTest(name = "with executionId={0}, clientId={1}, centerId={2}")
  @MethodSource("provideInvalidRequests")
  void search_shouldReturn400BadRequest_whenBodyIsInvalid(
          Long executionId, Long clientId, Long centerId) throws Exception {

    // Given
    WorkerAttendanceSearchRequest request =
            WorkerAttendanceSearchRequest.builder()
                    .executionId(executionId)
                    .clientId(clientId)
                    .centerId(centerId)
                    .build();

    // When & Then
    mockMvc
            .perform(
                    post(ENDPOINT_URI)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
  }

  private void stubAttendancesWorkerSearch(WorkerAttendanceResponse response) {
    WorkerAttendanceSearch criteria = WorkerAttendanceSearch.builder().build();
    Page<WorkerAttendance> page = new PageImpl<>(List.of(WorkerAttendance.builder().build()));
    when(searchMapper.fromRequest(any(WorkerAttendanceSearchRequest.class))).thenReturn(criteria);
    when(workerAttendanceService.searchIntegratedWithEnrollment(
            any(WorkerAttendanceSearch.class), any(Pageable.class)))
            .thenReturn(page);
    when(mapper.toResponse(any(WorkerAttendance.class))).thenReturn(response);
  }
}

```

- **Persistence Layer slice test**:
  - Use `@DataJpaTest`.
  - This configures an in-memory database.
  - File path: `src/test/java/...`
  - Extends `BaseEntityTest`
  - Persistence layer slice test for entities ends with `EntityIT` (e.g., `UnplannedExecutionEntityIT`).

```java
class WorkerAttendanceEntityTest extends BaseEntityTest {

  private static final String PATH = "/test/worker-attendance.pdf";
  private static final String NAME = "worker-attendance.pdf";

  @Test
  void create_shouldReturnWorkerAttendance_whenSuccess() {
    Long executionId = 10L;
    Long clientId = 20L;
    Long centerId = 30L;
    Long workerId = 40L;
    Long userDeleteId = 50L;
    LocalDateTime deletedDate = LocalDateTime.now();

    WorkerAttendanceEntity toPersist = new WorkerAttendanceEntity();
    toPersist.setExecutionId(executionId);
    toPersist.setClientId(clientId);
    toPersist.setCenterId(centerId);
    toPersist.setWorkerId(workerId);
    toPersist.setPath(PATH);
    toPersist.setName(NAME);
    WorkerAttendanceType type = WorkerAttendanceType.DEVICE;
    toPersist.setType(type);
    toPersist.setDeletedAt(deletedDate);
    toPersist.setDeletedBy(userDeleteId);

    WorkerAttendanceEntity persisted = getEntityManager().persist(toPersist);

    assertThat(persisted).isNotNull();
    assertThat(persisted.getId()).isNotNull();
    assertThat(persisted.getExecutionId()).isEqualTo(executionId);
    assertThat(persisted.getClientId()).isEqualTo(clientId);
    assertThat(persisted.getCenterId()).isEqualTo(centerId);
    assertThat(persisted.getWorkerId()).isEqualTo(workerId);
    assertThat(persisted.getPath()).isEqualTo(PATH);
    assertThat(persisted.getName()).isEqualTo(NAME);
    assertThat(persisted.getType()).isEqualTo(type);
    assertThat(persisted.getDeletedAt()).isEqualTo(deletedDate);
    assertThat(persisted.getDeletedBy()).isEqualTo(userDeleteId);
  }
}
```

- **Integration Test**:
  - Use `@SpringBootTest` ONLY when needed to load context and external integrations (Avoid Integration Overhead).
  - Use `@Sql` to initialize required data. Create separate SQL files in `src/integrationTest/resources/<domain>/`,
    where
    `<domain>` is the model of the register.
  - File path: `src/integrationTest/java/...` (or your specific integration source set).
  - Extends `BaseIT`
  - Integration test ends with `IT` (e.g., `SearchExecutionIT`).

```java
class SearchExecutionIT extends BaseIT {

  public static final String ENDPOINT = "/execution/search";

  @Test
  @Sql("/com/vitaly/pt/formacion/infrastructure/api/execution/execution_technician_id_111.sql")
  void search_shouldReturn200OkAndContainsExecution_whenFilteringByIdFound() {
    // Given
    long executionId = 111L;
    ExecutionSearchRequest requestBody =
            ExecutionSearchRequest.builder().technicianIds(List.of(executionId)).build();
    // When
    ResponseEntity<ExecutionSearchResponse> response =
            getRestTemplate().postForEntity(ENDPOINT, requestBody, ExecutionSearchResponse.class);
    // Then
    assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
    ExecutionSearchResponse body = response.getBody();
    assertThat(body).isNotNull();
    UserResponse technician = body.technician();
    assertThat(technician.id()).isEqualTo(executionId);
  }
}
```

- **Method naming**: Use `snake_case` formatted as: `[method]_[expectedBehavior]_[scenario]`.
  - Example: `update_shouldReturn400BadRequest_whenInvalidInput()`
- **Structure**: Tests MUST strictly follow the BDD pattern using `// Given`, `// When`, `// Then` comments.
- **Constraints**:
  - Avoid using Reflection in tests.
  - Chain AssertJ assertions fluently (e.g., `assertThat(result).isNotNull().hasSize(3);`).
  - Use Long for objects id.
- **ValidationError** is composed of (domain, field, cause).
  - Domain is given by `*Validator.getDomain()`.
  - Field is given by a static constant in the validator (e.g. FIELD_START_TIME, FIELD_TRAINING_PLAN_ID).
  - Cause is given by a ValidationError constant (e.g. REQUIRED, ALREADY_EXISTS).

## Logging

- Use `@Slf4j` annotation from Lombok for logging to avoid boilerplate code with Logger instances.
- Log at appropriate levels: `DEBUG`, `INFO`, `WARN`, `ERROR`.
- Include contextual information in logs (e.g., request IDs, user IDs).
- Avoid logging sensitive information.
- Use structured logging for better log management.
- Format log messages with placeholders (e.g., `{}`) instead of string concatenation.

