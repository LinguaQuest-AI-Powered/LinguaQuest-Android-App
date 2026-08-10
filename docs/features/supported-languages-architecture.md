# Supported Languages Caching & Architecture

## 1. Overview
The LinguaQuest application requires a list of "Supported Languages" for multiple core features, including Onboarding, Settings (changing native language), and the MindReader feature. Previously, this data was fetched via an authentication endpoint (`/auth/languages`) and tightly coupled to the Auth feature.

To adhere to Clean Architecture, this cross-cutting concern has been centralized in the `core/language` package. The app now fetches the supported languages once at startup (during the Splash screen) and caches them persistently.

## 2. Caching Strategy

The app utilizes a standard "warm up" cache strategy:
1. **Splash Screen Prefetch**: During app launch, the `SplashViewModel` invokes the `PrefetchSupportedLanguagesUseCase`.
2. **Persistent Storage**: The `SupportedLanguagesRepository` fetches the languages from the remote API and stores them locally using DataStore via `SupportedLanguagesCacheDataSource`.
3. **Instant Access**: Other screens (Onboarding, Settings, etc.) inject `GetSupportedLanguagesUseCase`. The repository returns the cached data instantly, eliminating redundant network calls and loading spinners across the app.

## 3. Architecture Structure

The Supported Languages functionality is isolated within the `core/language` module:

```text
app/src/main/java/com/iti/linguaquest/core/language/
 ├── data/
 │    ├── datasource/
 │    │    ├── local/SupportedLanguagesCacheDataSource.kt
 │    │    └── remote/LanguageRemoteDataSource.kt
 │    └── repository/SupportedLanguagesRepositoryImpl.kt
 ├── domain/
 │    ├── repository/SupportedLanguagesRepository.kt
 │    └── usecase/
 │         ├── GetSupportedLanguagesUseCase.kt
 │         └── PrefetchSupportedLanguagesUseCase.kt
```

## 4. Usage in Other Features

- **Onboarding & Settings**: The `LanguagesViewModel` and `SettingViewModel` use `GetSupportedLanguagesUseCase` to instantly display the available native and target languages.
- **MindReader**: `ResolveMindReaderNativeLanguageCodeUseCase` and `ResolveMindReaderTargetLanguageCodeUseCase` use `GetSupportedLanguagesUseCase` to resolve language codes for voice features.
- **Auth (Syncing Native Language)**: The `SyncUserNativeLanguageUseCase` handles updating the user's native language upon login. When the backend provides the full `LanguageOption` object during standard login, it uses it directly. If only the `nativeLanguageId` is available (e.g., in some OAuth Profile Completion flows), it falls back to using the `GetSupportedLanguagesUseCase` to resolve the full language details.
