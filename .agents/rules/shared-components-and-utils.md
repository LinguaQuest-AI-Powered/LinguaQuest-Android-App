---
trigger: always_on
---

---

description: This rule enforces the use of existing shared components and utilities in LinguaQuest, prevents reinventing the wheel, and enforces strict clean code practices with zero comments.
mode: always
---

# LinguaQuest Shared Components, Utils & Clean Code Rules

## 1. Shared UI Components (Strict Reusability)

- **Location**: All globally reusable Compose components are centralized in `app/src/main/java/com/iti/linguaquest/core/sharedComponents/`.
- **Reuse First**: Before creating a new button, text field, dialog, or app bar, you must check for existing components in this directory (e.g., `AppButton.kt`, `LinguaQuestTopAppBar.kt`, `dialog/`, `snackbar/`).
- **Creation**: If a new component is highly generic and reusable, it must be created in this `core/sharedComponents/` directory, not within a specific feature module.

## 2. Utility Functions & Extensions

- **Location**: General utility functions, helpers, and Kotlin extensions live in `app/src/main/java/com/iti/linguaquest/core/utils/`.
- **Reuse First**: Before writing custom validation, date formatting, or platform-specific helpers, check for existing utilities (e.g., `ValidationUtils.kt`, `ImageUriUtils.kt`, `SpeechManager.kt`).
- **Separation of Concerns**: Utility functions must remain separate from business logic (UseCases) and UI components.
