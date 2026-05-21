---
trigger: glob
globs: "src/test/java/**/*EntityIT.java"
---

## Persistence Layer Slice Testing Rules

- **Annotation:** Use `@DataJpaTest` to configure an automatic, isolated in-memory H2 database.
- **Base Class:** Must extend `BaseEntityTest`.
- **Structure:** Strictly follow `// Given`, `// When`, `// Then`. Use `getEntityManager().persist(...)` to seed data
  and AssertJ fluent assertions to verify the state.
- **Naming:** Class names must end with `EntityIT.java`.
- Example:

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