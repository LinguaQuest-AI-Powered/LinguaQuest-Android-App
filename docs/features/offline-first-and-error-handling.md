# Offline-First Architecture & Global Error Handling

This document summarizes the offline-first implementation for the Home, Profile, and Gallery screens, as well as the global error handling standardization across LinguaQuest.

## 1. Offline-First Screens (Home, Profile, Gallery)

### Architectural Overview
- **Immediate Local Cache Display**: `HomeScreen`, `ProfileScreen`, and `GalleryScreen` load data reactively from local Room database flows (`observeHomeSummary()`, `getCachedProfileUseCase()`, `getWordsWithImagesUseCase()`) immediately upon rendering.
- **Removal of Full-Screen Blocking Spinners**: `LoadingView` and `ErrorView` full-screen overlays have been eliminated from standard screen navigation flow.
- **Silent Background Synchronization**: Background sync functions update local storage silently when network calls succeed. If network calls fail (e.g. while offline), existing cached data remains displayed without interruption.
- **Pull-To-Refresh**: Manual pull-to-refresh (`PullToRefreshBox`) triggers background syncing across screens and provides transient feedback.

---

## 2. Global Error Handling & Localization Standardization

### Key Components

1. **`strings.xml` Error Resources**:
   - Replaced technical strings (`Error NO Internet`, `error_bad_request`, `error_unauthorized`) with clear, user-friendly copy.
   - Added specific string resources for authentication, storage, and network errors.

2. **Data Error Mapping (`DataErrorUiTextMapper.kt`)**:
   - Centralized `LinguaQuestDataError.toUiText()` mapping for `Remote`, `Auth`, `Firestore`, `Local`, and `CustomServerMessage` error types.

3. **`ErrorView` Composable Overload**:
   - Added `ErrorView(message: UiText, ...)` overload in `ErrorView.kt` to allow passing `UiText` objects directly.

4. **ViewModel Error Consistency**:
   - Standardized `SnackbarEvent` emission and `ErrorView` usage across ViewModels (`MapViewModel`, `LeaderboardViewModel`, `AchievementViewModel`, `ReviewViewModel`, `AllWorldsViewModel`, `GameResultViewModel`, `GalleryViewModel`).
