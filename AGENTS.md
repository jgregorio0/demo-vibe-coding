# AGENTS.md

## Architecture

- **Hexagonal Architecture (Ports and Adapters)**

To prevent architectural erosion, you must adhere to the direction of dependencies:

- The **Domain Layer** is isolated. It must not import any classes from application, infrastructure, or framework libraries (e.g., Spring, JPA, Hibernate).
- The **Application Layer** can only import from the domain layer. It must not import anything from infrastructure.
- The **Infrastructure Layer** may import from both application and domain.

## Folder structure

All Java source code lives under `src/main/java/dev/jgregorio/demo/vibe/`. You must strictly categorize files into these three main layers:

```text
src/main/java/dev/jgregorio/demo/vibe/
├── domain/                  # Core business logic (Pure Java, Framework-Free)
│   ├── model/               # Pure domain entities, aggregates, and value objects
│   └── exception/           # Business-specific exceptions (e.g., UserNotFoundException)
│
├── application/             # Core Use Case flow Orchestration
│   ├── port/                # Boundary interfaces
│   │   ├── in/              # Driving/Primary Ports (Use Case interfaces)
│   │   └── out/             # Driven/Secondary Ports (SPIs, DB/External API interfaces)
│   └── service/             # Domain Services implementing primary/in-ports
│
└── infrastructure/          # Frameworks, configs, and adapter implementations
    ├── adapter/             # Concrete implementations of ports
    │   ├── in/              # Driving Adapters
    │   │   └── web/         # REST Controllers, Web DTOs, and Request/Response Mappers
    │   └── out/             # Driven Adapters
    │       └── db/          # Spring Data Repositories, JPA Entities, DB Mappers
    └── config/              # Spring Beans configuration, Security, and Setup
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


## Code Principles
- Use Hexagonal Architecture.
- Use Clean Code.
- Use SOLID principles.
- Use KISS principle.

## Code Style

- **Encoding**: Use UTF-8 encoding.
- **Naming**: Use descriptive names for classes, methods, and variables.
- **Formatting**: Use google-java-format. Use gradlew spotlessApply to format code.
- **Immutability:** Use final, Records and immutables. Avoid mutating objects. Do not use `.forEach()` in Stream API for mutations.
- **Null Safety:** Use `Objects.isNull()` and `Objects.nonNull()` instead of direct null checks. Check emptiness/nullness before string/collection operations.
- **Readability:** Wrap multiple conditions into a meaningful boolean variable. Prefer early returns and avoid unnecessary `else` statements.
- **Comments:** Avoid comments (code must be self-explanatory). 
- **Throws clauses**: Do not use `throws` clauses. Use unchecked exceptions instead.
- **Dependency Injection:** NEVER use `@Autowired`. Always prefer Constructor Injection using Lombok's `@RequiredArgsConstructor`.
- **String Formatting:** When overriding `toString()`, strictly use `String.format("Resource[id=%d]", id);`.
- **Commits:** Use Conventional Commits.
- Avoid `var` keyword, prefer explicit types.

## Annotations

- **`@Service`**: For business logic classes.
- **`@Repository`**: For data access classes that extend JPA repositories or interact with the database.
- **`@RestController`**: For web controllers.
- **`@Component`**: For generic Spring components.
- **`@Configuration`**: For Spring configuration classes.
- **`@ConfigurationProperties`**: For binding related properties avoid multiple `@Value` annotations. From more than 2 properties, consider using this annotation.
- **`@Valid`**: To validate inputs in controllers.

## Lombok Annotations

- Use `@RequiredArgsConstructor` from Lombok for dependency injection via constructor.
- Use `@Slf4j` from Lombok for logging.
- Use `@Builder` for object creation.
- Avoid `@Data` annotation; prefer `@Getter` and `@Setter` for granular control.

## Logging

- Use `@Slf4j` annotation from Lombok for logging to avoid boilerplate code with Logger instances.
- Log at appropriate levels: `DEBUG`, `INFO`, `WARN`, `ERROR`.
- Include contextual information in logs (e.g., request IDs, user IDs).
- Avoid logging sensitive information.
- Use structured logging for better log management.
- Format log messages with placeholders (e.g., `{}`) instead of string concatenation.

