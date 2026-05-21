---
trigger: glob
globs: "src/test/java/**/*Test.java"
---

## Unit Testing Rules

- **Focus:** Testing Domain Logic and pure behavior (Black-Box testing).
- **Context:** Pure Java 25. Do NOT load the Spring Context. Use `@Mock` and `@InjectMocks`.
- **Realism:** Minimize mocks. Build real objects using the Builder Pattern instead of mocking data structures.
- **Assertions:** Use state-based assertions via AssertJ fluent API. Avoid interaction testing (`verify(mock)`).
- **Imports:** Always use static import: `import static org.assertj.core.api.Assertions.assertThat;`.
- **Structure:** Must strictly follow BDD layout using comments: `// Given`, `// When`, `// Then`.
- **Naming:** Use `snake_case` formatted as `[method]_[expectedBehavior]_[scenario]`.
- **Constraints:** No Reflection. Use `Long` for object IDs. Classes must end with `Test` (e.g., `ExecutionTest.java`).

- Example:
```java

class DomainTest {

  private static final Long ID = 1L;

  @Test
  void fromId_shouldCreateExecutionWithOnlyId() {
    // When
    Execution execution = Execution.from(ID);

    // Then
    assertThat(execution.id()).isEqualTo(ID);
    assertThat(execution.type()).isNull();
    assertThat(execution.date()).isNull();
    assertThat(execution.course()).isNull();
  }
}
```