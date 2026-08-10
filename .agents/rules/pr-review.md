---
trigger: always_on
---

---

description: Enforces strict guidelines for Pull Request (PR) code reviews. Must be applied whenever the user requests a PR review, auditing code strictly on the current branch without running builds or making modifications.
mode: always
---

# LinguaQuest Pull Request (PR) Review Protocol

## 1. Execution Constraints (Strict)

When requested to review a PR or analyze changes on the current branch, you must adhere strictly to the following constraints:

* **Current Branch Scope:** Audit only the changes present on the active working branch versus the target branch.
* **Static Analysis Only:** Conduct the audit purely by reading and analyzing the source code.
* **ZERO Execution:** You are strictly prohibited from running Gradle commands (e.g., `./gradlew assembleDebug`), running unit tests, or executing any scripts or builds during a PR review.
* **ZERO Modifications:** You are strictly prohibited from editing, creating, refactoring, or deleting any code or project files during the review. The repository must remain completely untouched.

---

## 2. Review Audit Criteria

Your analysis must check the PR code against all project rules and architectural boundaries:

### A. Clean Architecture Standards

* **Boundary Leaks:** Ensure ViewModels do **not** interact directly with Repositories or Data Sources. All business logic must be delegated to UseCases in `domain/usecase/`.
* **Domain Purity:** Ensure UseCases and Domain entities do not import Android/UI packages or depend on presentation/data layers.

### B. Project Rules & Conventions

* **MVI Architecture:** Verify naming conventions (`[Feature]State`, `[Feature]Intent`, `[Feature]Effect`), usage of `StateFlow`/`SharedFlow`, and intent processor functions (`onIntent`).
* **Compose UI:** Ensure stateful Composables end in `Screen` and stateless ones end in `Content`/`Card`/`View`. Check for missing `modifier: Modifier = Modifier`, hardcoded colors, or hardcoded UI strings (must use `UiText` or `stringResource`).
* **Jetpack Navigation 3:** Verify type-safe `NavKeys`, `@Serializable` key structures in `Screens.kt`, and absence of string-based routing.
* **Error Handling & Logging:** Verify `LinguaQuestResult` usage. Enforce strict **Timber** logging rules (no `e.printStackTrace()`, no `android.util.Log`, and ensure scratchpad logs like `Timber.d("here 1")` are removed).
* **Clean Code & Style:** Enforce **ZERO fully qualified inline package calls** (must use explicit `import` statements or import aliases) and **ZERO boilerplate code comments**.
* **Unit Testing Standards:** Verify that test files (if included) adhere to JUnit 4, MockK, Turbine, `MainDispatcherRule`, and `Given / When / Then` comment structures.

---

## 3. Mandatory Review Output Format

Your entire response must be a structured Markdown analysis report containing the following sections:

```markdown
# Pull Request Review Report

## 1. Overview
- **Branch Checked**: `[Current Branch Name]`
- **Status**: `[APPROVED / ACTION REQUIRED]`

---

## 2. Clean Architecture Violations
*(If none found, explicitly state: "No Clean Architecture violations detected.")*

* **File:** `path/to/File.kt` (Line X)
  * **Violation:** Description of architectural boundary breach.
  * **Required Fix:** Explanation of how to correct the layer separation.

---

## 3. Project Rules & Conventions Violations
*(If none found, explicitly state: "No rule violations detected.")*

* **File:** `path/to/File.kt` (Line X)
  * **Category:** `[MVI / Compose UI / Navigation / Logging / Imports / Clean Code]`
  * **Violation:** Specific rule broken (e.g., hardcoded string, inline fully qualified package name, missing Timber usage).
  * **Required Fix:** Concrete guidance on how to bring the code into compliance.

---

## 4. Summary & Next Steps
- Brief bulleted summary of key changes requested before merging.
