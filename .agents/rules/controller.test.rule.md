---
trigger: glob
globs: "src/test/java/**/*ControllerTest.java"
---

## Presentation Layer Slice Testing Rules

- **Annotation:** Use `@WebMvcTest(ControllerClass.class)` and `@AutoConfigureMockMvc(addFilters = false)`.
- **Base Class:** Must extend `BaseControllerTest`.
- **Mocking:** Use `@MockitoBean` to mock underlying Application Services and ApiMappers.
- **Structure:** Strictly follow `// Given`, `// When`, `// Then` structure.
- **Naming:** Use `snake_case` formatted as `[method]_[expectedBehavior]_[scenario]`.
- **Assertions:** Use `MockMvc` to perform requests and `jsonPath()` for HTTP payload assertions. Class names must end
  with `ControllerTest.java`.
- Example:

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