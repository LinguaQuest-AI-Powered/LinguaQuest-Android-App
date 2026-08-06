---
trigger: always_on
---

---

description: This rule dictates how errors, exceptions, and logging are handled across the LinguaQuest project using LinguaQuestResult and Timber.
mode: always
---

# LinguaQuest Error Handling & Logging Rules

## 1. Domain Error Handling (LinguaQuestResult)

- **Result Wrapper**: Never throw raw exceptions for expected domain or network failures. All repository and UseCase operations must return the custom `LinguaQuestResult` interface (e.g., `LinguaQuestResult.Success`, `LinguaQuestResult.Error`).
- **Processing**: ViewModels must evaluate the `LinguaQuestResult` and map domain errors to appropriate `UiText` messages to be displayed to the user via state or effects.

## 2. Logging Constraints (Strict Timber Enforcement)

- **No Stack Traces**: `e.printStackTrace()` is strictly prohibited anywhere in the codebase.
- **No Android Log**: Standard `android.util.Log` calls (e.g., `Log.e`, `Log.d`) are strictly forbidden.
- **Timber Logging**: All logging must strictly use **Timber** (e.g., `Timber.e(exception, "Message")`, `Timber.d("Message")`).
- **No Manual Tagging/Debug Checks**: Do not manually declare `TAG` constants or wrap Timber calls in `if (BuildConfig.DEBUG)` checks; Timber's tree initialization in `Application` handles environment safety automatically.

## 3. Pull Request Logging Standards (Contextual vs. Scratchpad)

- **Keep Contextual Logs**: It is standard practice to leave meaningful `Timber.d`, `Timber.i`, and `Timber.e` logs that provide valuable context for future debugging (e.g., `Timber.e(exception, "Failed to evaluate boss stage result")` or `Timber.d("Starting VoiceGame with level ID: $levelId")`).
- **Remove Scratchpad Logs**: "Breadcrumb" logs used temporarily for tracing execution (e.g., `Timber.d("here 1")`, `Timber.d("value: $x")`) must be removed before merging to the `dev` branch.
- **No PII**: Never log Personally Identifiable Information (PII) like passwords, tokens, or sensitive user data under any log level.
- **No Hot-Path Logs**: Avoid logging inside performance-critical paths such as Compose `onDraw` phases or tight `while` loops, as string evaluation can cause performance hits even if the log is not printed.
