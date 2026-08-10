# Bottom Navigation Architecture

## Overview
LinguaQuest utilizes Jetpack Navigation 3 for all deep-level navigation, but explicitly avoids `NavDisplay` backstack reordering for bottom navigation tabs. 

Instead, the bottom navigation relies on Compose's built-in `SaveableStateHolder` and conditional rendering.

## Why avoid NavDisplay for tabs?
When using `NavDisplay` with `rememberViewModelStoreNavEntryDecorator()`, the decorator ties the lifecycle of a `ViewModel` strictly to the presence of its `NavKey` in the `NavBackStack`. 

Standard bottom navigation implementations often require bringing a tab to the front of the backstack using a pattern like `removeAt(index)` followed by `add(item)`. However, even this momentary removal from the backstack causes the Navigation 3 decorators to instantly dispose of the `ViewModelStore`. This destroys the `ViewModel`, forcing it to re-instantiate and re-fetch network data every time the user returns to a previously visited tab.

## The Solution: SaveableStateHolder
To preserve tab states and ViewModels without keeping multiple tabs composed in the UI tree simultaneously, LinguaQuest uses a single `currentTab` state variable combined with a `SaveableStateHolder`.

### 1. State Preservation
```kotlin
val saveableStateHolder = rememberSaveableStateHolder()
var currentTab by rememberSaveable { mutableStateOf(BottomNavScreen.Home) }

saveableStateHolder.SaveableStateProvider(key = currentTab) {
    when (currentTab) {
        BottomNavScreen.Home -> HomeScreen(...)
        // ...
    }
}
```
`SaveableStateHolder` automatically saves and restores any `rememberSaveable` state (such as scroll positions or UI toggles) associated with a specific key (`currentTab`) when it leaves and re-enters the composition.

### 2. ViewModel Scoping
Because the `NavDisplay` is no longer managing the tabs, how do the ViewModels survive when the tab leaves the composition?

The entire `MainScreen` is wrapped inside a single `entry<RootScreen.Main>` in the root `AppNavigation`. When a tab calls `hiltViewModel()`, it traverses up the composition tree and resolves the ViewModel against the `ViewModelStore` owned by `entry<RootScreen.Main>`. 

Because `entry<RootScreen.Main>` remains continuously active in the root backstack, all tab ViewModels are safely preserved in its store for the duration of the session, regardless of which tab is currently visible.

## Handling Logout
To ensure that all tab ViewModels and saved states are properly destroyed upon user logout, the `RootScreen.Main` route acts as a unique session boundary.

```kotlin
@Serializable
data class Main(val sessionId: Long) : RootScreen
```

When navigating to the main screen after a successful login:
```kotlin
navigateSingleTop(RootScreen.Main(System.currentTimeMillis()))
```
Because the `sessionId` changes on every login, Jetpack Navigation treats it as a completely new destination. It allocates a brand new `ViewModelStore` and `SaveableStateHolder` for the new session, guaranteeing that no stale profile data or UI states bleed over from a previous user's session.
