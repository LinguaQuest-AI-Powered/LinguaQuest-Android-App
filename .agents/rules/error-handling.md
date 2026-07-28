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