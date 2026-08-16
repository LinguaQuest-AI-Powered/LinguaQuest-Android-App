# Languages Architecture & Synchronization

## Overview
This document outlines how the application manages user language preferences, synchronization of the UI language (Native Language), and the architecture for managing "My Languages" (Target Languages) within the Home screen.

---

## 1. Authentication & Native Language Sync

### `SyncUserNativeLanguageUseCase`
- Orchestrates the synchronization of the app's UI language with the user's `nativeLanguage`.
- **Process (Standard Login/Google Sign-In)**:
  1. Instantly extracts the ID, Name, and Code from the `nativeLanguage` object returned in the auth response. **(No extra backend/cache fetch required!)**
  2. Updates the `UserPreferencesRepository` with the Native Language ID, Name, and Target App Language (language code).
  3. Triggers `LanguageManager.changeLanguage(code)` to immediately update the UI.
- **Process (OAuth Profile Completion Fallback)**:
  1. Fetches available languages from the cache or backend (`GetAuthLanguagesUseCase`).
  2. Finds the corresponding language using the `nativeLanguageId`.
  3. Saves the data and updates the `LanguageManager` as described above.

---

## 2. "My Languages" (Target Languages) Data Flow

The `LanguagesRepoImpl` implements an **offline-first caching strategy** using Room (`LanguagesDao`) to manage the user's target languages.

### Cache-First Fetching (`getMyLanguages`)
- The repository exposes `getMyLanguages()` as a continuous `Flow` backed by `channelFlow`.
- **Local DB Emits First:** It immediately observes `languagesDao.getMyLanguagesFlow()` and emits the cached list.
- **Lazy Remote Sync:** If (and only if) the local cache is empty, it makes a remote API call (`remoteDataSource.getMyLanguages()`), then writes the response to the local DB. This prevents redundant network calls every time the user opens the "Add Languages" screen.

### Error-Resilient Mutations (`addLanguages`, `removeLanguages`)
- When a user adds or removes a language, the remote API is called first.
- **Success:** The new list returned by the backend is immediately saved to the local database, which automatically pushes the update to the UI observing the `Flow`.
- **Failure Fallback:** If the mutation API fails, the repository fires a fallback background call to `/languages/mine` to resynchronize the local cache with the backend. This ensures the UI doesn't fall out of sync in case of partial successes or malformed responses.

---

## 3. Active Language Switching (NavStack Reset Architecture)

Switching the active target language drastically changes the app's entire learning state across multiple bottom navigation tabs (Home, Gallery, Profile). To guarantee zero data pollution and a robust reset, the application utilizes a "Clean Slate" architecture via a global event bus.

### Flow Breakdown
1. **Selection & Confirmation:** 
   - User taps a language in the Bottom Sheet.
   - `MyLanguagesViewModel` caches this in `languagePendingActivation` and shows an `AppDialog`.
2. **Instant Loading State (UX Feedback):**
   - Upon confirming, `MyLanguagesViewModel` immediately emits a `SwitchingLanguage` effect.
   - The `HomeScreen` catches this and fires `HomeIntent.PrepareLanguageSwitch` to shift `HomeViewModel` into `DataStatus.Loading`, displaying a full-screen spinner *instantly*, and simultaneously dismissing the bottom sheet.
3. **API Call & Global Event Emission:**
   - `MyLanguagesViewModel` triggers `setActiveLanguageUseCase(languageId)` (POST `/languages/mine/active`).
   - On success, it emits `SessionEvent.LanguageChanged` into the global `SessionEventBus`.
4. **The NavStack Reset:**
   - `AppNavigation` listens to the global `SessionEventBus`. When it receives `LanguageChanged`, it completely clears the `rootBackStack` and navigates to a brand-new instance of `RootScreen.Main(System.currentTimeMillis())`.
   - By leveraging Jetpack Navigation's behavior, this forcefully destroys all existing ViewModels (Home, Profile, Gallery) tied to the old language state. When the new `MainScreen` boots up, all ViewModels are freshly initialized and automatically fetch data for the newly active language.

### Edge Case: Network Desynchronization
If the `setActiveLanguageUseCase` request successfully completes on the server, but the client disconnects before receiving the response, the local Room database (`LanguagesDao`) will fail to update. 
- **The Symptom:** The global Home screen might fetch the new language upon reconnection, but `MyLanguagesViewModel` (which only reads from local cache) will still show the old language as active.
- **The Solution:** The `refreshFromRemote(isPullToRefresh = true)` inside `HomeViewModel` explicitly fires `RefreshMyLanguagesUseCase()`. This forces a direct call to `remoteDataSource.getMyLanguages()` and completely overwrites the local Room cache. If desynchronization occurs, the user can easily pull-to-refresh on the Home screen to self-correct the entire application state.

---

## 4. Settings Native Language Change Flow

Changing the application/native language in Settings also invokes the global `SessionEvent.LanguageChanged` flow to ensure total consistency across the application.

### Step-by-Step Flow:
1. **Language Selection:** User taps a native language option inside `LanguageSelectionBottomSheet`.
2. **Confirmation Modal (`AppDialog`):** `SettingViewModel` sets `pendingLanguage` on `LanguagesUiState` and presents an `AppDialog` modal prompting the user to confirm the switch.
3. **Execution on Confirmation:**
   - `ChangeAppLanguageUseCase` saves the new language code & name in preferences, updates `LanguageManager` (applying Android `LocaleManager` or locale configuration), and syncs the native language with the backend.
   - `ClearLanguageCacheUseCase` invokes `sessionManagerRepository.clearLanguageDependentData()` to immediately purge stale cached home/profile/gallery data.
   - `SettingViewModel` emits `SessionEvent.LanguageChanged` onto `SessionEventBus`.
4. **Pop to HomeScreen & MainScreen Re-instantiation:**
   - `AppNavigationScreen` catches `SessionEvent.LanguageChanged`, clears the backstack (popping Settings screen), and routes to `RootScreen.Main(System.currentTimeMillis())`.
   - All ViewModels (Home, Profile, Gallery) are cleanly re-instantiated and reload fresh localized content and stats directly from the network/cache.

