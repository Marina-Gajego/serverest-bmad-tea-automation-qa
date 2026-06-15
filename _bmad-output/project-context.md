---
project_name: 'serverest-api-automation-qa'
user_name: 'Marina'
date: '2026-06-13'
sections_completed: ['technology_stack', 'architecture_patterns', 'naming_conventions', 'critical_rules']
patterns_discovered: 12
last_updated: '2026-06-13'
---

# Project Context for AI Agents

_This file contains critical rules and patterns that AI agents must follow when implementing code in this project. Focus on unobvious details that agents might otherwise miss._

---

## Technology Stack & Versions

### Core Dependencies
- **Java**: 17 (source & target)
- **Maven**: 3.11.0+
- **REST Assured**: 5.4.0
- **Cucumber**: 7.15.0 (with PicoContainer DI)
- **JUnit**: 5.10.2 (Jupiter) + 1.10.1 (Platform Suite)
- **Lombok**: 1.18.30
- **Jackson**: 2.17.0
- **AssertJ**: 3.25.1
- **SLF4J**: 2.0.7 + Logback 1.4.14
- **JSON Schema Validator**: 5.4.0
- **Java Faker**: 1.0.2
- **Apache Commons Lang3**: 3.14.0

### Build Configuration
- Encoding: UTF-8
- Compiler: Maven Compiler Plugin v3.11.0
- Annotation Processing: Lombok via POM annotationProcessorPaths
- Test Scope: All testing dependencies use `<scope>test</scope>`

---

## Architecture & Layer Responsibilities

### Layered Architecture Pattern
```
StepsDefinitions → Services → Factory → Model ← ScenarioContext
                                ↓
                            REST Assured
                                ↓
                            ServeRest API
```

**Layer Responsibilities:**
1. **Steps Definitions** (`stepsDefinitions/`): Gherkin step implementations
   - Accept parameters from scenarios
   - Call factory to build requests
   - Call services to execute API calls
   - Assert responses via StepDefinitions
   - Store results in ScenarioContext

2. **Services** (`services/`): HTTP API execution
   - Single method per HTTP operation
   - Use REST Assured fluent API
   - Log all requests with `.log().all()`
   - Return `io.restassured.response.Response` object
   - Inherit `@Slf4j` for logging

3. **Factory** (`factory/`): Test data builders
   - Create domain models with test data
   - Support parameterized test scenarios
   - Use Faker for random data generation
   - Return ready-to-serialize model objects

4. **Model** (`model/`): Data transfer objects (DTOs)
   - Lombok `@Data` + `@Builder(toBuilder = true)`
   - `@JsonInclude(JsonInclude.Include.NON_NULL)` for clean JSON
   - `@NoArgsConstructor` + `@AllArgsConstructor` required
   - Serializable via Jackson

5. **ScenarioContext** (`context/ScenarioContext.java`): State container
   - Singleton injected via PicoContainer
   - Maintains: Response, payload, authToken, testData map
   - Domain-specific properties (email, userId, productName, etc.)
   - `clearAll()` called between scenarios to reset state

### Domain Organization
- **Paths.java**: Constants for BASE_URL (env-aware), endpoints
- Package structure: `br.com.marina.qa.[layer]/[Domain]`
  - Example: `br.com.marina.qa.services.Login.LoginService`
  - Example: `br.com.marina.qa.factory.Users.CreateUsersFactory`

---

## Naming Conventions & Code Style

### Class Naming
- **Models**: `[Domain]Model` (e.g., `LoginModel`, `CreateUsersModel`, `PutUsersByIdModel`)
- **Services**: `[Domain][Operation]Service` (e.g., `LoginService`, `GetUsersService`, `DeleteUsersService`)
- **Factories**: `[Domain][Operation]Factory` (e.g., `LoginFactory`, `CreateUsersFactory`)
- **StepDefinitions**: `[Domain]Steps` (e.g., `LoginSteps`, `UsersSteps`)

### Method Naming
- Services: Single public method, verb-first
  - `login(Object payload)` → returns `Response`
  - `getUsers(String params)` → returns `Response`
  - `deleteUser(String userId)` → returns `Response`
- Factories: `[operation]` or `with[Field]` for builders
  - `create()` with proper data
  - `withEmail(String email)` for builder patterns
- StepDefinitions: `I[verb][description]` (Gherkin binding)
  - `iSendAPOSTRequestToTheAuthenticationEndpoint()`

### Constant Naming
- **UPPER_SNAKE_CASE** in `Paths.java`
- Examples: `BASE_URL`, `LOGIN_ENDPOINT`, `USERS_ENDPOINT`, `PRODUCTS_ENDPOINT`
- Environment override: `System.getProperty()` → `System.getenv()` → default

### Package Structure
```
src/test/java/br/com/marina/qa/
├── context/ScenarioContext.java           # State container
├── factory/[Domain]Factory.java           # Test data builders
├── model/[Domain]/[Operation]Model.java   # DTOs with @Builder
├── paths/Paths.java                       # API endpoints & BASE_URL
├── runner/RunnerTest.java                 # Cucumber runner
├── services/[Domain]/[Operation]Service.java  # HTTP operations
└── stepsDefinitions/[Domain]Steps.java    # Gherkin implementations

src/test/resources/
├── features/[Domain]/[Feature].feature    # BDD scenarios
└── schemas/[Domain]/[operation].schema.json  # JSON Schema contracts
```

---

## Critical Implementation Rules

### Rule 1: Lombok Annotation Requirements
**ALL Models must follow this pattern:**
```java
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class [Domain]Model {
    private String field1;
    private String field2;
}
```
- `toBuilder = true` enables `.toBuilder()` for test variations
- `@JsonInclude(JsonInclude.Include.NON_NULL)` prevents empty fields in JSON requests
- Always include all four annotations (not partial)

### Rule 2: Service Logging Pattern
**ALL Services must use SLF4J @Slf4j:**
```java
@Slf4j
public class [Domain]Service {
    public Response operation(Object payload) {
        Response response = given()
            .baseUri(BASE_URL)
            .basePath(ENDPOINT)
            .contentType("application/json")
            .body(payload)
            .when()
            .log().all()          // ← MANDATORY for debugging
            .post();
        
        log.info("Operation completed with status: {}", response.getStatusCode());
        return response;
    }
}
```
- Use `.log().all()` on REST Assured `when()` chain
- Log response status code in info level after execution
- Never return response without storing reference first

### Rule 3: REST Assured Request Construction
**Standard pattern for all HTTP operations:**
```java
Response response = given()
    .baseUri(BASE_URL)                          // Always use constant
    .basePath(ENDPOINT_CONSTANT)                // From Paths.java
    .contentType("application/json")            // Or appropriate media type
    .body(payload)                              // Model object (auto-serialized)
    .when()
    .log().all()                                // Required
    .post();                                    // or .get() / .put() / .delete()
```
- Static import: `import static io.restassured.RestAssured.given;`
- Always specify `baseUri()` + `basePath()` separately
- Content type always `"application/json"` for REST API
- No query parameters in `given()` — use `.queryParam()` or `.pathParam()` as needed

### Rule 4: ScenarioContext State Management
**Store all cross-step data in ScenarioContext:**
```java
// In StepDefinitions:
public class [Domain]Steps {
    private ScenarioContext scenarioContext;  // Injected via PicoContainer
    
    @When("I do something")
    public void iDoSomething() {
        // Store response
        scenarioContext.setResponse(response);
        
        // Store extracted data
        scenarioContext.setAuthToken(response.jsonPath().getString("token"));
        scenarioContext.setUserId(response.jsonPath().getString("_id"));
    }
}
```
- PicoContainer auto-injects `ScenarioContext` to StepDefinitions constructors
- Always use `setResponse()` after API calls for later assertions
- Extract token/ID for subsequent requests
- Call `clearAll()` is handled by Cucumber hooks (verify in runner)

### Rule 5: Factory Pattern for Test Data
**Factories must return ready-to-use Models:**
```java
public class LoginFactory {
    public LoginModel createValidLogin() {
        return LoginModel.builder()
            .email("test@example.com")
            .password("password123")
            .build();
    }
    
    public LoginModel loginWithEmail(String email) {
        return LoginModel.builder()
            .email(email)
            .password(Faker.password())  // Use JavaFaker
            .build();
    }
}
```
- Use `JavaFaker` for random data: `new Faker().internet().emailAddress()`
- Always chain `.builder()` with `.build()`
- Support multiple variations with overloaded methods
- Never return Model with `@Nullable` validation errors

### Rule 6: StepDefinition Binding & Assertions
**Use Cucumber annotations + AssertJ for assertions:**
```java
@When("I send a POST request to the {string} endpoint")
public void iSendPOSTRequest(String endpoint) {
    // Action
}

@Then("The response status code should be {int}")
public void statusCodeShouldBe(int expectedCode) {
    Response response = scenarioContext.getResponse();
    assertThat(response.getStatusCode()).isEqualTo(expectedCode);  // AssertJ
}
```
- Use `@Given`, `@When`, `@Then` annotations
- Parameter extraction: `{string}`, `{int}`, `{float}`, `{word}`
- Assertions use AssertJ fluent API: `assertThat(...).isEqualTo(...)`
- All assertion imports: `static org.assertj.core.api.Assertions.assertThat`

### Rule 7: Contract Validation with JSON Schema
**Always validate response contracts:**
```java
@Then("The response contract should match {string}")
public void validateContract(String schemaPath) {
    Response response = scenarioContext.getResponse();
    
    response.then()
        .assertThat()
        .body(matchesJsonSchemaInClasspath(schemaPath));
}
```
- Schema files stored in `src/test/resources/schemas/[Domain]/[operation].schema.json`
- Use REST Assured's `matchesJsonSchemaInClasspath()`
- Import: `import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;`
- One schema per endpoint + HTTP method

### Rule 8: Environment Configuration
**BASE_URL must support environment override:**
```java
// In Paths.java (ALREADY IMPLEMENTED)
public static final String BASE_URL = System.getProperty("base.url", 
    System.getenv("BASE_URL") != null ? System.getenv("BASE_URL") : "http://localhost:3000");
```
- Priority: JVM property `-Dbase.url=` → environment variable `BASE_URL` → default localhost:3000
- Never hardcode API host in services
- Support Docker, local, and cloud deployments

### Rule 9: Feature File Organization
**Gherkin features follow domain separation:**
```gherkin
@regression @Login
Feature: Validate login api in different scenarios

  @id=scenario_id
  Scenario: Login with valid credentials - (POST /login)
    Given I have registered user
    When I send a POST request to the authentication endpoint
    Then The response status code should be 200
    And The response contract should match "schemas/Login/login.schema.json"
```
- Tag with `@regression` and `@Domain`
- Include HTTP method in scenario name: `(POST /login)`
- One feature file per domain/endpoint
- Use `@id=` for traceability to requirements

### Rule 10: Error Handling & Negative Scenarios
**All new test coverage must include error cases:**
- Status codes: 400 (validation), 401 (auth), 404 (not found), 500 (server)
- Validation errors: message field, field-specific errors
- Contract validation: negative schemas for error responses
- Duplicate scenarios for edge cases (empty, null, invalid format)

### Rule 11: Dependency Injection via PicoContainer
**Cucumber uses PicoContainer for ScenarioContext injection:**
```java
public class [Domain]Steps {
    private ScenarioContext scenarioContext;
    
    // PicoContainer auto-injects via constructor injection
    public [Domain]Steps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }
}
```
- No `@Inject` annotation needed (Cucumber auto-wires constructors)
- Always accept `ScenarioContext` as constructor param when needed
- Avoid static state; use instance fields for injected dependencies

### Rule 12: Test Execution & Cucumber Runner
**RunnerTest.java must support tag-based filtering:**
```bash
# Run all regression tests
mvn test

# Run specific tag
mvn test -Dcucumber.filter.tags="@Login"

# Run multiple tags (AND)
mvn test -Dcucumber.filter.tags="@regression and @Login"
```
- Features run via JUnit Platform engine
- Runner aggregates all steps, schemas, and features
- No direct method calls; only Gherkin execution

---

## Common Implementation Mistakes (Avoid These!)

❌ **DO NOT:**
- Hardcode URLs in services (use `Paths.BASE_URL`)
- Create Models without `@Builder(toBuilder = true)`
- Store state in static variables (use `ScenarioContext`)
- Forget `.log().all()` in REST Assured chains
- Mix assertion libraries (use only AssertJ)
- Omit `@JsonInclude(JsonInclude.Include.NON_NULL)` from Models
- Create Models without all 4 Lombok annotations
- Ignore contract validation (always validate against schema)
- Use `System.out.println()` instead of `@Slf4j` logging

✅ **DO:**
- Use static imports for REST Assured fluently
- Inherit `@Slf4j` on all Service classes
- Store responses in `ScenarioContext` immediately after API call
- Extract and store tokens for subsequent authenticated requests
- Validate both success and error response contracts
- Use JavaFaker for random test data
- Organize features by domain + HTTP method
- Support environment variable overrides for configuration
- Reuse test data via factory methods

---

## Project Conventions Summary

| Aspect | Convention | Example |
|--------|-----------|---------|
| **Package** | domain-based | `br.com.marina.qa.services.Login` |
| **Class** | Domain + Role | `LoginService`, `GetUsersFactory` |
| **Method** | camelCase, verb-first | `login()`, `getUsers()` |
| **Constant** | UPPER_SNAKE_CASE | `BASE_URL`, `LOGIN_ENDPOINT` |
| **Feature** | Domain directory | `features/Login/Login.feature` |
| **Schema** | operation + .schema | `schemas/Login/login.schema.json` |
| **Model** | @Data @Builder @JsonInclude | All 4 annotations required |
| **Service** | @Slf4j single method | Log status code always |
| **Logging** | SLF4J info level | `log.info("message", args)` |
| **Assertion** | AssertJ fluent | `assertThat(...).isEqualTo(...)` |

---

## Quick Reference: Adding a New Test

### Step 1: Create Model (if new domain)
```java
// src/test/java/br/com/marina/qa/model/NewDomain/NewDomainModel.java
@Data @Builder(toBuilder = true) @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewDomainModel { ... }
```

### Step 2: Create Service
```java
// src/test/java/br/com/marina/qa/services/NewDomain/GetNewDomainService.java
@Slf4j
public class GetNewDomainService {
    public Response get() {
        Response response = given()
            .baseUri(BASE_URL).basePath(NEW_DOMAIN_ENDPOINT)
            .when().log().all().get();
        log.info("GET completed with status: {}", response.getStatusCode());
        return response;
    }
}
```

### Step 3: Create Feature
```gherkin
# src/test/resources/features/NewDomain/NewDomain.feature
@regression @NewDomain
Feature: New domain tests
  Scenario: Describe test - (HTTP_METHOD /endpoint)
    Given ...
    When ...
    Then The response status code should be 200
```

### Step 4: Create Schema(s)
```json
// src/test/resources/schemas/NewDomain/operation.schema.json
{ "type": "object", "properties": { ... }, "required": [...] }
```

### Step 5: Create StepDefinition
```java
// src/test/java/br/com/marina/qa/stepsDefinitions/NewDomainSteps.java
public class NewDomainSteps {
    private ScenarioContext scenarioContext;
    
    public NewDomainSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }
    
    @When("I call the new domain endpoint")
    public void iCallNewDomainEndpoint() {
        Response response = new GetNewDomainService().get();
        scenarioContext.setResponse(response);
    }
}
```

---

**Last Updated:** June 13, 2026  
**Applicable To:** All AI Agents, Code Generators, Refactoring Tools

