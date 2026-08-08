# Language Synchronization & Preferences

## Overview
This document outlines how the application manages user language preferences, specifically regarding the synchronization of the UI language with the user's selected native language upon authentication and OAuth profile completion.

## Architecture & Components

### 1. `AuthCacheDataSource` (DataStore)
A dedicated DataStore named `auth_cache_prefs` is provisioned to store authentication-related caches, keeping them strictly separated from general user preferences.
- Stores the `/auth/languages` JSON response to avoid unnecessary backend calls.

### 2. `LanguageManager` (Core)
- Moved to the `com.iti.linguaquest.core.language` package for broader accessibility.
- Responsible for updating the app's `Locale` and restarting activities/recomposing when the UI language changes.

### 3. `SyncUserNativeLanguageUseCase`
- Orchestrates the synchronization of the app's UI language with the user's `nativeLanguage`.
- **Capabilities**: Can match the language either by `nativeLanguageName` (used during standard login/Google sign-in) or `nativeLanguageId` (used during OAuth profile completion).
- **Process**:
  1. Fetches available languages from the cache or backend (`GetAuthLanguagesUseCase`).
  2. Finds the corresponding `AuthLanguageOptionDto` using the name or ID.
  3. Updates the `UserPreferencesRepository` with the Native Language ID, Name, and Target App Language (language code).
  4. Triggers `LanguageManager.changeLanguage(code)` to immediately update the UI.

## OAuth Profile Completion Flow
When a user signs in with Google for the first time without having selected their languages during onboarding, `signInWithGoogle` returns `profileComplete = false`.

1. **Navigation**: `LoginViewModel` / `SignUpViewModel` navigates the user to the `OAuthLanguageSelection` screen.
2. **Submission**: Upon language selection, the user clicks "Continue".
3. **Execution (`CompleteOAuthProfileUseCase`)**:
   - The UI view models call `CompleteOAuthProfileUseCase` with the selected `nativeLanguageId` and `targetLanguageId`.
   - The backend validates the request.
   - On success (`200 OK`), the UseCase delegates to `SyncUserNativeLanguageUseCase` using the `nativeLanguageId`.
4. **Duplicate Request Protection**:
   - Both `LoginViewModel` and `SignUpViewModel` implement an `if (_state.value.isLoading) return` check before calling `CompleteOAuthProfileUseCase` to prevent race conditions or `409 Conflict` errors caused by double-taps on the UI button.
