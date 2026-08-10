# Screen UI Architecture & Component Decomp Standard

This document defines LinguaQuest's standard pattern for structuring Jetpack Compose screen components and orchestrating complex UI elements (like animations and overlays). This pattern was established during the Home screen refactor and should be used as a reference for building future complex screens.

## 1. The Screen Architecture Hierarchy

A complex screen should never consist of a single, monolithic `@Composable` function. Instead, it must be broken down into layers with distinct responsibilities:

### A. The Orchestrator (e.g., `HomeScreen.kt`)
- **Role:** The entry point wrapper.
- **Responsibilities:**
  - Instantiates ViewModels (via Hilt).
  - Collects MVI states (`StateFlow`) and maps them to local state.
  - Collects one-off effects (`SharedFlow`) and handles navigation callbacks.
  - Manages `LaunchedEffect`s for timers, sounds, or side-effects.
  - Defines the core layout framework (typically a `Box`).
  - Implements the `Crossfade` router based on `DataStatus` (see `screen-state-management.md`).
- **Strict Rule:** Must NOT contain detailed layout logic, inline dialogs, or deeply nested UI components.

### B. The Core Content (e.g., `HomeContent.kt`)
- **Role:** Renders the primary scrollable content of the screen when `DataStatus` is `Loaded` or `Refreshing`.
- **Responsibilities:**
  - Manages staggered entrance animations (see Section 2).
  - Lays out the primary feature components (cards, lists, headers).

### C. The Overlays (e.g., `HomeOverlays.kt`)
- **Role:** A single, stateless composable containing all pop-ups for the screen.
- **Responsibilities:**
  - Dialogs (e.g., `DailyMissionDialog`, `HomeDailyRewardDialog`).
  - Bottom Sheets (e.g., `MyLanguagesBottomSheet`).
- **Strict Rule:** Never pass ViewModels into `HomeOverlays`. Pass the required raw `State` objects and expose intent callbacks (lambdas) back to the Orchestrator.

### D. Floating Elements (e.g., `HomeFabs.kt`)
- **Role:** Extracts Floating Action Buttons, sticky banners, or toolbars.
- **Why extract?** FABs often require complex coordinate tracking (`onGloballyPositioned`) and state management (e.g., `fabBounds` tracking) which bloats the Orchestrator if kept inline.

---

## 2. Reusable Staggered Animations

LinguaQuest features a custom animation utility built to eliminate boilerplate when implementing sequential entrance animations. 

**Location:** `core/sharedComponents/animations/StaggeredAnimation.kt`

### How to use it

**1. Initialize the State:**
In your content composable, initialize the animation state with the total number of items you plan to animate.
```kotlin
// count = number of items to stagger
val animationState = rememberStaggeredAnimationState(count = items.size)
```
> **Note:** `rememberStaggeredAnimationState` uses `rememberSaveable` under the hood. This ensures animations only play on the *initial* mount and will not annoyingly replay if the user rotates their phone.
> **Dynamic Lists:** The state scales dynamically. If your list size increases (e.g., after a pull-to-refresh), the new items will automatically trigger their stagger animation while existing items remain visible.

**2. Wrap Items in `StaggeredAnimatedItem`:**
Wrap each distinct UI element in the `StaggeredAnimatedItem` wrapper. Provide it an `index` (0, 1, 2...) and specify the entrance animation type from `LingoEntranceAnimations`.
```kotlin
StaggeredAnimatedItem(
    index = 0,
    state = animationState,
    enter = LingoEntranceAnimations.popUpVertically(offset = -60)
) {
    LanguageProgressCard(...)
}

StaggeredAnimatedItem(
    index = 1,
    state = animationState,
    enter = LingoEntranceAnimations.popUpHorizontally(offset = 200)
) {
    WordCaptureCard(...)
}
```

## 3. Resolving Crossfade Recomposition Issues (The "Double Animation" Bug)

When managing Pull-to-Refresh with a `Crossfade`, a common bug occurs where staggered animations replay when a background refresh completes. 

**The Cause:** `Crossfade` rebuilds its internal composition from scratch whenever the `targetState` object changes, causing states like `rememberStaggeredAnimationState` to reset.

**The Solution:** The `StatefulContentContainer` shared component abstracts this away completely. Under the hood, it groups `Loaded` and `Refreshing` under the exact same target state identifier so `Crossfade` does not trigger a transition.

**Correct Implementation:**
Use `StatefulContentContainer` which inherently handles this fix:
```kotlin
StatefulContentContainer(
    dataStatus = state.dataStatus,
    onRetry = { ... },
    onRefresh = { ... }
) {
    MainContent(...)
}
```
