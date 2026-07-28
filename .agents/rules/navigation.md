---
trigger: always_on
---

---
description: This rule enforces the usage of Jetpack Navigation 3 conventions for all routing in the LinguaQuest project.
mode: always
---

# LinguaQuest Navigation Architecture Rules

## 1. Developer-Owned Back Stack (State-Backed Navigation)
*   **No String Routes:** The project strictly avoids legacy string-based routing and XML navigation graphs[cite: 1].
*   **State List:** Navigation is driven by a developer-owned back stack managed via `rememberNavBackStack(initialRoute)`[cite: 1]. The back stack acts as a standard list of states[cite: 1].
*   **Navigating:** Navigation is performed by adding or removing items from this stack (e.g., `rootBackStack.navigateSingleTop(Screen)`, `rootBackStack.removeLastOrNull()`, `rootBackStack.clear()`)[cite: 1].

## 2. Type-Safe Serialization (NavKeys)
*   **Route Definitions:** All routes are strongly typed and must be defined in `Screens.kt`[cite: 1].
*   **Sealed Interfaces:** Destinations must be grouped under a `@Serializable sealed interface` that extends `NavKey` (e.g., `RootScreen : NavKey`, `NestedScreen : NavKey`)[cite: 1].
*   **Data Objects/Classes:** Individual screens are defined as a `@Serializable data object` for screens without arguments, or a `@Serializable data class` for screens requiring arguments (e.g., `Review(val wordId: Int)`)[cite: 1].

## 3. UI Display & Content Resolution
*   **NavDisplay:** The `NavDisplay` composable is the container used to render the back stack[cite: 1].
*   **Entry Provider:** The `entryProvider` block inside `NavDisplay` acts as the centralized router, which maps the strongly typed `NavKey` instances to their respective `@Composable` screens[cite: 1].
*   **Decorators:** To preserve states and ViewModels during navigation, `NavDisplay` must include `rememberSaveableStateHolderNavEntryDecorator()` and `rememberViewModelStoreNavEntryDecorator()`[cite: 1].

## 4. Nested Navigation
*   **Isolated Sub-flows:** Sub-flows (such as Bottom Navigation or Game Flow) must maintain their own separate `sealed interface` keys (e.g., `NestedScreen`, `GameFlowScreen`)[cite: 1].
*   **Isolated Stacks:** This structure allows a nested `NavDisplay` to manage its own isolated back stack independent of the root stack[cite: 1].