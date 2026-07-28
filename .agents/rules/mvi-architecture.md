---
trigger: always_on
---

---
description: This rule enforces LinguaQuest's Clean Architecture layer separation and strict MVI (Model-View-Intent) naming conventions. It must be applied whenever creating or modifying ViewModels, UseCases, or business logic.
mode: always
---

# LinguaQuest Clean Architecture & MVI Rules

## 1. Architectural Boundaries (Strict)
- **ViewModels** must *never* directly interact with Repositories or Data Sources.
- All business logic must be delegated to granular, single-responsibility `UseCase` classes located in the `domain/usecase/` package.
- UseCases are injected into ViewModels via Hilt (`@Inject` constructor).

## 2. State & Events Management
- ViewModels must expose continuous UI state using `StateFlow<T>`.
- ViewModels must expose one-time UI events (like navigation or toasts) using `SharedFlow<T>`.
- Background flow observers (like network monitoring) must utilize `.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), ...)` to automatically pause flow collection in the background.

## 3. MVI Naming Conventions
When building a new feature or screen, the MVI contract must follow these exact naming prefixes based on the feature name (e.g., `[Feature]State`):
- **State (`[Feature]State`)**: An immutable `data class` holding the continuous UI state.
- **Intent (`[Feature]Intent`)**: A `sealed class` (or `sealed interface`) defining all possible user actions (e.g., `LoginIntent.LoginClicked`).
- **Effect (`[Feature]Effect`)**: A `sealed class` defining one-time UI events (e.g., `LoginEffect.NavigateToForgotPassword`).
- **Processing**: The UI interacts with the ViewModel exclusively through a centralized intent processor function (e.g., `fun onIntent(intent: [Feature]Intent)`).