---
trigger: always_on
---

---

trigger: always_on
---

---

description: This rule enforces LinguaQuest's testing standards, including the strict prohibition of unsolicited test generation, mandatory tech stack, and test structure conventions.
mode: always
---

# LinguaQuest Unit Testing Standards

## 1. The Opt-In Directive (Strict)

* **No Unsolicited Testing:** You are strictly prohibited from generating, updating, or suggesting unit tests unless the user explicitly requests testing in their prompt.
* **Focus on Production:** Unless instructed otherwise, your primary focus must always be on implementing, refactoring, or fixing production code.

## 2. Testing Architecture & Technology Stack

When instructed to write tests, you must strictly use the established LinguaQuest testing stack. Do not introduce alternative testing libraries (e.g., JUnit 5, Mockito).

* **Core Runner:** JUnit 4.
* **Mocking:** MockK (`coEvery`, `every`, `coVerify`, `verify`, `match`).
* **Flow Testing:** Turbine (`app.cash.turbine`) must be used for testing `StateFlow` and `SharedFlow` emissions.
* **Coroutines:** `kotlinx-coroutines-test` (`runTest`).
* **Dispatchers:** Use `MainDispatcherRule` to safely execute ViewModel coroutine scopes.

## 3. Test Structure & Naming Conventions

To maintain consistency and readability, all test functions must adhere to the following structure:

* **Naming Convention:** Test function names must follow the strict format: `methodName_expectedBehavior_whenCondition` (e.g., `evaluatePronunciation_returnsFailure_whenServiceThrowsException`).
* **Given / When / Then:** Inside the test body, you must strictly organize the logic using `// Given`, `// When`, and `// Then` comments to separate setup, execution, and assertion phases. *(Note: This is an explicit exception to the general clean code no-comments rule, applicable only to test files).*

## 4. Exhaustive Branching & Coverage

Tests must not be limited to the "happy path." You must actively write tests for:

* **Error Boundaries & Exceptions:** Validate exception mapping to `LinguaQuestResult.Failure` and ensure UI effects (e.g., Snackbars) are triggered correctly.
* **Edge Cases:** Test null responses, invalid JSON structures, and fallback mechanisms.
* **Conditional Logic:** If a feature has branching logic (e.g., rating >= 6 yields coins, rating < 6 yields 0), you must test *all* branches.
* **Lifecycle & Cleanup:** Ensure `onCleared()` behaviors, resource disposal, and job cancellations are verified.

## 5. Layer-Specific Testing Requirements

* **Presentation (ViewModels):** You must test the `onIntent` processor. Use Turbine to capture and assert both continuous state updates and one-off effects.
* **Domain (UseCases):** Verify delegation to repositories, default parameter resolution, and the seamless propagation of `LinguaQuestResult`.
* **Data/Remote Layer:** Validate complex parsing (e.g., AI JSON responses, WAV header modifications) and error mapping from remote exceptions to domain errors.

## 6. Code Quality inside Tests

* **Rule Inheritance:** Production clean code rules apply to test files.
* **No Stack Traces:** Do not use `e.printStackTrace()` even in mocked error responses; use Timber if logging is required in mock setups.
* **Strings:** Continue to use `UiText` for assertions involving user-facing messages.
