# 🚀 Spring Boot REST API Production-Ready Template

A modern, robust, and production-ready Spring Boot REST API template built with Spring Boot 4, Java 25, and PostgreSQL. This project is structured following clean architecture principles, incorporating industry best practices for security, database migrations, testing, and containerization.

---

## 🛠️ Tech Stack & Key Features

- **Language & Runtime:** Java 25 & Spring Boot 4
- **Build Tool:** Gradle
- **Version control:** Git & GitHub
- **Database:** PostgreSQL (Production) & H2 (In-memory testing)
- **Persistence:** Spring Data JPA & Hibernate
- **Database Migrations:** Flyway
- **Security:** Spring Security (JWT-based authentication & authorization)
- **API Documentation:** OpenAPI 3 / Swagger UI
- **Testing:** JUnit 5, Mockito, Testcontainers (for database integration tests)
- **Code Quality:** Lombok, MapStruct (for DTO mapping)
- **Containerization:** Docker & Docker Compose
- **Monitoring:** Spring Boot Actuator & Prometheus
- **Code Styling:** Spotless

---

## 🏗️ Project Structure

This project follows a **Hexagonal Architecture** (Ports and Adapters) pattern, separating the core business logic from infrastructure and delivery mechanisms:

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

---

## 🚦 Getting Started

### Prerequisites

To run this application locally, you will need:
* **Java Development Kit (JDK) 25**
* **Gradle** (or use the included `./gradlew` wrapper)
* **Docker & Docker Compose** (for running the database container)

### Step 1: Clone and Prepare Environment

Clone the repository and navigate to the project root:

```bash
git clone https://github.com/jgregorio0/demo-vibe-coding.git
cd demo-vibe-coding
```

### Step 2: Spin Up Infrastructure

Start the PostgreSQL database service using Docker Compose:

```bash
docker-compose up -d
```

> [!NOTE]
> The default configuration spins up a PostgreSQL instance on port `5432` with a database named `spring_db` and user/password as configured in `docker-compose.yml`.

### Step 3: Run the Application

Execute the Gradle bootRun command:

```bash
./gradlew bootRun
```

Alternatively, run it using active profiles (e.g., `dev`):

```bash
./gradlew bootRun -Dspring-boot.run.profiles=dev
```

Once started, the application will be accessible at: `http://localhost:8080`

---

## 📖 API Documentation

We use **Swagger / OpenAPI** to document the REST endpoints. 

- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI JSON Spec:** `http://localhost:8080/v3/api-docs`

### Sample Endpoints Reference

| Method | Endpoint | Description | Auth Required |
|:---|:---|:---|:---|
| **POST** | `/api/v1/auth/register` | Register a new user | No |
| **POST** | `/api/v1/auth/login` | Login and retrieve JWT Token | No |
| **GET** | `/api/v1/users/me` | Get current authenticated user profile | Yes (Bearer Token) |
| **GET** | `/api/v1/items` | Retrieve paginated items | No |
| **POST** | `/api/v1/items` | Create a new item | Yes (Admin Role) |

---

## ⚙️ Configuration File (`application.yml`)

The primary configurations are stored in `src/main/resources/application.yml`. You can override properties using environment variables or profile-specific files like `application-dev.yml` or `application-prod.yml`.

Example database configuration fragment:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:spring_db}
    username: ${DB_USER:postgres}
    password: ${DB_PASSWORD:postgres}
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate # Database schema managed by Flyway
    show-sql: false
    properties:
      hibernate:
        format_sql: true
  flyway:
    enabled: true
```

---

## 🧪 Testing

The test suite contains both unit and integration tests.

### Run All Tests
```bash
./mvnw test
```

### Key Testing Libraries:
- **JUnit 5 & AssertJ** for expressive assertions.
- **Mockito** for mocking dependencies in unit tests.
- **MockMvc** for testing the web layer without launching a full server.
- **Testcontainers** for automated integration testing against a real database instance (requires Docker).

---

## 🐳 Docker Deployment

To build a production Docker image of the Spring Boot application:

```bash
# Build the JAR file
./mvnw clean package -DskipTests

# Build the Docker image
docker build -t spring-boot-api-demo:latest .

# Run the container
docker run -p 8080:8080 --name spring-api-app spring-boot-api-demo:latest
```

---

## 🔒 Security Implementation Checklist

- [x] JWT verification and generation middleware.
- [x] Password hashing using BCrypt.
- [x] CORS configured for authorized origins.
- [x] Security headers set up (XSS Protection, Content Security Policy, HSTS).
- [ ] Rate limiting (e.g., using bucket4j or api gateway).

---

## 🤝 Contributing

Contributions are welcome! Please read the contributing guidelines and submit a Pull Request.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
