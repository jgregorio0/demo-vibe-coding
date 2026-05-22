# Rocket Feature

## Description

- **As a** manager, **I can** register, update, and delete rockets in the fleet, **so that** I can schedule flights for bookings.
- **As a** user, **I can** retrieve details of a specific rocket or list all available rockets, **so that** I can match customer requirements to the correct rocket capabilities.
- **As an** admin, **I can** enforce validation rules on rocket range and capacity, **so that** unsafe configurations and overbooking are prevented.

## Logic Flowchart

Each operation follows a validate → check existence → persist pattern. Write operations (register/update) enforce domain rules before touching the database, while read/delete operations resolve existence first.

```mermaid
flowchart TD
    Client([HTTP Client]) --> Controller[RocketController]

    Controller -->|POST /api/v1/rockets| Register[registerRocket]
    Controller -->|GET /api/v1/rockets/:id| Retrieve[retrieveRocket]
    Controller -->|GET /api/v1/rockets| List[listRockets]
    Controller -->|PUT /api/v1/rockets/:id| Update[updateRocket]
    Controller -->|DELETE /api/v1/rockets/:id| Delete[deleteRocket]

    Register --> ValidateInput{Valid request?}
    ValidateInput -->|No| 400[400 Bad Request]
    ValidateInput -->|Yes| MapToDomain1[Map to Domain]
    MapToDomain1 --> ValidRange1{Valid range enum?}
    ValidRange1 -->|No| 422A[422 Validation Error]
    ValidRange1 -->|Yes| DomainValidate1[RocketValidator.validate]
    DomainValidate1 -->|Invalid| 422B[422 Validation Error]
    DomainValidate1 -->|Valid| CheckDuplicate{Name already exists?}
    CheckDuplicate -->|Yes| 409[409 Conflict]
    CheckDuplicate -->|No| SaveNew[persistencePort.save]
    SaveNew --> 201[201 Created]

    Retrieve --> FindById{Exists by ID?}
    FindById -->|No| 404A[404 Not Found]
    FindById -->|Yes| 200A[200 OK]

    List --> FindAll[persistencePort.findAll]
    FindAll --> 200B[200 OK - List]

    Update --> ValidateInput2{Valid request?}
    ValidateInput2 -->|No| 400B[400 Bad Request]
    ValidateInput2 -->|Yes| FindExisting{Exists by ID?}
    FindExisting -->|No| 404B[404 Not Found]
    FindExisting -->|Yes| MapToDomain2[Map to Domain with existing ID]
    MapToDomain2 --> DomainValidate2[RocketValidator.validate]
    DomainValidate2 -->|Invalid| 422C[422 Validation Error]
    DomainValidate2 -->|Valid| CheckNameConflict{Name taken by another?}
    CheckNameConflict -->|Yes| 409B[409 Conflict]
    CheckNameConflict -->|No| SaveUpdated[persistencePort.save]
    SaveUpdated --> 200C[200 OK]

    Delete --> ExistsCheck{Exists by ID?}
    ExistsCheck -->|No| 404C[404 Not Found]
    ExistsCheck -->|Yes| DeleteById[persistencePort.deleteById]
    DeleteById --> 204[204 No Content]
```

## Sequence Diagram

Shows the full request lifecycle for each operation, from HTTP client through the web adapter, application service, and persistence adapter down to the database.

```mermaid
sequenceDiagram
    participant C as HTTP Client
    participant RC as RocketController
    participant M as RocketApiMapper
    participant S as RocketService
    participant P as RocketPersistenceAdapter
    participant DB as Database

    Note over C,DB: Register Rocket (POST /api/v1/rockets)
    C->>RC: POST /api/v1/rockets {name, range, capacity}
    RC->>M: toDomain(request)
    M-->>RC: Rocket domain object
    RC->>S: registerRocket(rocket)
    S->>S: RocketValidator.validate(rocket)
    S->>P: findByName(name)
    P->>DB: SELECT * FROM rockets WHERE name = ?
    DB-->>P: empty
    P-->>S: Optional.empty()
    S->>P: save(rocket)
    P->>DB: INSERT INTO rockets
    DB-->>P: RocketEntity
    P-->>S: Rocket
    S-->>RC: Rocket
    RC->>M: toResponse(rocket)
    M-->>RC: RocketResponse
    RC-->>C: 201 Created

    Note over C,DB: Retrieve Rocket (GET /api/v1/rockets/{id})
    C->>RC: GET /api/v1/rockets/{id}
    RC->>S: retrieveRocket(id)
    S->>P: findById(id)
    P->>DB: SELECT * FROM rockets WHERE id = ?
    DB-->>P: RocketEntity
    P-->>S: Optional<Rocket>
    S-->>RC: Rocket
    RC->>M: toResponse(rocket)
    M-->>RC: RocketResponse
    RC-->>C: 200 OK

    Note over C,DB: Update Rocket (PUT /api/v1/rockets/{id})
    C->>RC: PUT /api/v1/rockets/{id} {name, range, capacity}
    RC->>M: toDomain(request)
    M-->>RC: Rocket domain object
    RC->>S: updateRocket(id, rocket)
    S->>P: findById(id)
    P->>DB: SELECT * FROM rockets WHERE id = ?
    DB-->>P: RocketEntity
    P-->>S: Optional<Rocket>
    S->>S: RocketValidator.validate(updatedRocket)
    S->>P: findByName(name)
    P->>DB: SELECT * FROM rockets WHERE name = ?
    DB-->>P: result
    P-->>S: Optional<Rocket>
    S->>P: save(updatedRocket)
    P->>DB: UPDATE rockets SET ...
    DB-->>P: RocketEntity
    P-->>S: Rocket
    S-->>RC: Rocket
    RC->>M: toResponse(rocket)
    M-->>RC: RocketResponse
    RC-->>C: 200 OK

    Note over C,DB: Delete Rocket (DELETE /api/v1/rockets/{id})
    C->>RC: DELETE /api/v1/rockets/{id}
    RC->>S: deleteRocket(id)
    S->>P: existsById(id)
    P->>DB: SELECT COUNT(*) FROM rockets WHERE id = ?
    DB-->>P: true
    P-->>S: true
    S->>P: deleteById(id)
    P->>DB: DELETE FROM rockets WHERE id = ?
    DB-->>P: void
    P-->>S: void
    S-->>RC: void
    RC-->>C: 204 No Content
```

## Class Diagram

Shows the hexagonal architecture structure: domain model and validator at the core, application layer with use case port and service, and infrastructure adapters on the outside.

```mermaid
classDiagram
    direction TB

    class Rocket {
        +Long id
        +String name
        +RocketRange range
        +Integer capacity
    }

    class RocketRange {
        <<enumeration>>
        SUBORBITAL
        ORBITAL
        INTERPLANETARY
    }

    class RocketValidator {
        +validate(Rocket rocket) void
    }

    class ManageRocketUseCase {
        <<interface>>
        +registerRocket(Rocket) Rocket
        +retrieveRocket(Long) Rocket
        +listRockets() List~Rocket~
        +updateRocket(Long, Rocket) Rocket
        +deleteRocket(Long) void
    }

    class RocketPersistencePort {
        <<interface>>
        +save(Rocket) Rocket
        +findById(Long) Optional~Rocket~
        +findByName(String) Optional~Rocket~
        +findAll() List~Rocket~
        +deleteById(Long) void
        +existsById(Long) boolean
    }

    class RocketService {
        -RocketPersistencePort persistencePort
        +registerRocket(Rocket) Rocket
        +retrieveRocket(Long) Rocket
        +listRockets() List~Rocket~
        +updateRocket(Long, Rocket) Rocket
        +deleteRocket(Long) void
    }

    class RocketController {
        -ManageRocketUseCase useCase
        -RocketApiMapper mapper
        +registerRocket(RocketRequest) ResponseEntity
        +retrieveRocket(Long) ResponseEntity
        +listRockets() ResponseEntity
        +updateRocket(Long, RocketRequest) ResponseEntity
        +deleteRocket(Long) ResponseEntity
    }

    class RocketRequest {
        +String name
        +String range
        +Integer capacity
    }

    class RocketResponse {
        +Long id
        +String name
        +String range
        +Integer capacity
    }

    class RocketApiMapper {
        <<interface>>
        +toDomain(RocketRequest) Rocket
        +toResponse(Rocket) RocketResponse
    }

    class RocketPersistenceAdapter {
        -RocketRepository repository
        -RocketPersistenceMapper mapper
    }

    class RocketEntity {
        +Long id
        +String name
        +RocketRange range
        +Integer capacity
    }

    class RocketRepository {
        <<interface>>
        +findByName(String) Optional~RocketEntity~
    }

    Rocket --> RocketRange
    RocketValidator ..> Rocket
    RocketService ..|> ManageRocketUseCase
    RocketService --> RocketPersistencePort
    RocketService ..> RocketValidator
    RocketController --> ManageRocketUseCase
    RocketController --> RocketApiMapper
    RocketApiMapper ..> Rocket
    RocketApiMapper ..> RocketRequest
    RocketApiMapper ..> RocketResponse
    RocketPersistenceAdapter ..|> RocketPersistencePort
    RocketPersistenceAdapter --> RocketRepository
    RocketEntity --> RocketRange
```

## Entity Relationship Diagram

The `rockets` table is the sole entity. It stores each rocket's unique name, range classification, and passenger capacity.

```mermaid
erDiagram
    ROCKETS {
        BIGSERIAL id PK
        VARCHAR(255) name "NOT NULL, UNIQUE"
        VARCHAR(50) range "NOT NULL"
        INT capacity "NOT NULL"
    }
```
