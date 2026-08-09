# Screen State Management (DataStatus Pattern)

This document outlines the standard pattern used in LinguaQuest for managing the data-fetching and UI state of a screen. This pattern was pioneered on the Home screen and serves as the architectural standard for all complex data-driven screens going forward.

## The Problem with Booleans

Previously, screens managed their states using a flat bag of booleans inside the MVI `State` class (e.g., `isLoading`, `hasError`, `isRefreshing`, `errorMessage`). This approach leads to several maintenance issues:
1. **Implicit State Combinations:** The UI logic becomes littered with complex `if` conditions (e.g., `if (!state.hasError && !state.isLoading)`).
2. **Impossible States Are Representable:** Nothing stops the application from accidentally being in an `isLoading = true` and `hasError = true` state simultaneously, leading to undefined UI behavior.
3. **Duplicated Logic:** Code to check if "cache exists" often gets duplicated across the ViewModel when determining whether to show a full-screen error or a transient error (Snackbar).

## The Standard Solution: `DataStatus` Sealed Interface

To solve this, we model the screen's fundamental data-fetching status using a single, unified Kotlin `sealed interface`.

### 1. The Sealed Interface

Define a dedicated status interface in your feature's contract file (e.g., `HomeStates.kt`).

```kotlin
import com.iti.linguaquest.core.sharedComponents.text.UiText

sealed interface DataStatus {
    data object Loading : DataStatus
    data object Loaded : DataStatus
    data object Refreshing : DataStatus
    data class Error(val message: UiText) : DataStatus
}
```

### 2. The Screen State

The main MVI `State` data class replaces its boolean flags with this single `DataStatus` property. Additionally, it implements a `hasData` computed property to centralize the check for whether the screen has enough cached data to render meaningfully.

```kotlin
data class HomeState(
    val dataStatus: DataStatus = DataStatus.Loading,
    val xp: Int = 0,
    val coins: Int = 0,
    val worlds: List<WorldItem> = emptyList(),
    // ... other data fields
) {
    // Determines if the UI can render gracefully even if a remote fetch fails.
    val hasData: Boolean get() = worlds.isNotEmpty()
}
```

### 3. ViewModel Logic (State Transitions)

The ViewModel no longer toggles boolean flags. Instead, it transitions explicitly between `DataStatus` objects. Crucially, the ViewModel utilizes the `hasData` property to determine how to handle failures.

- **Initial Load:** Start as `DataStatus.Loading`.
- **Cache Hit:** Transition to `DataStatus.Loaded`.
- **Pull-to-Refresh:** Transition to `DataStatus.Refreshing`.
- **Remote Failure WITH Cache (`hasData == true`):** Stay as `DataStatus.Loaded` (or `Refreshing`), but dispatch a one-off `SnackbarEvent` to inform the user of the failure.
- **Remote Failure WITHOUT Cache (`hasData == false`):** Transition to `DataStatus.Error(errorMessage)` to trigger a full-screen blocking error.

```kotlin
// Example Remote Fetch Logic
} else {
    val errorUiText = UiText.StringResource(R.string.error_generic)
    val hasCache = _state.value.hasData

    _state.update {
        it.copy(
            // If cache exists, stay Loaded. If no cache, go to Error.
            dataStatus = if (hasCache) DataStatus.Loaded else DataStatus.Error(errorUiText)
        )
    }

    // Inform the user transiently if they are looking at stale cache
    if (hasCache) {
        snackbarController.sendEvent(
            SnackbarEvent(
                message = errorUiText,
                type = SnackbarType.ERROR,
                actionLabel = UiText.StringResource(R.string.retry),
                onAction = { refreshFromRemote(isPullToRefresh = true) }
            )
        )
    }
}
```

### 4. UI Layer (`when` statements)

The Jetpack Compose UI code becomes completely declarative and mutually exclusive. You no longer need to check `!hasError`. 

The root of your screen uses a `Crossfade` (or `AnimatedContent`) driven by `state.dataStatus`, branching with a `when` statement:

```kotlin
Crossfade(
    targetState = state.dataStatus,
    modifier = Modifier.fillMaxSize()
) { dataStatus ->
    when (dataStatus) {
        // 1. Full Screen Loading
        is DataStatus.Loading -> {
            LoadingView(...)
        }
        
        // 2. Full Screen Error (No Cache)
        is DataStatus.Error -> {
            ErrorView(
                message = dataStatus.message,
                onRetry = { viewModel.onIntent(Intent.Retry) }
            )
        }
        
        // 3. Content is visible (either fully loaded or refreshing in background)
        is DataStatus.Loaded, is DataStatus.Refreshing -> {
            PullToRefreshBox(
                isRefreshing = dataStatus is DataStatus.Refreshing,
                onRefresh = { viewModel.onIntent(Intent.Refresh) }
            ) {
                // Main Screen Content Composable
                MainContent(state = state)
            }
        }
    }
}

// Any Floating Action Buttons or overlays should explicitly check the status:
if (state.dataStatus is DataStatus.Loaded || state.dataStatus is DataStatus.Refreshing) {
    FloatingActionButton(...)
}
```

## Summary of Benefits
1. **Unambiguous UI Rendering:** Impossible for `LoadingView` and `ErrorView` logic to overlap.
2. **"Pop-in" Bug Prevention:** By tightly scoping components to specific sealed states, UI elements (like FABs or cards) naturally unmount during transitions, preventing visual glitches.
3. **DRY Cache Checks:** The definition of what constitutes a "valid cache" is housed strictly inside `hasData` rather than duplicated across ViewModel operations.
