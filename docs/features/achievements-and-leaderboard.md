# Achievements and Leaderboard Integration

## Overview
This document details the architecture, data pipeline, and UI integration connecting real backend API data for both the **Achievements** and **Leaderboard** features across their standalone screens and within the **Profile** overview, as well as the interactive **Achievement Details Modal Bottom Sheet**.

---

## 1. Clean Architecture Stack

### A. Achievements Feature (`features/achivement`)
- **Remote Data Source**:
  - `AchievementApiService`: Retrofit interface targeting `GET /achievements` with optional query parameter `status` (`ALL`, `EARNED`, `LOCKED`).
  - `AchievementRemoteDataSource` / `AchievementRemoteDataSourceImpl`: Invokes API within `safeApiCall` and unwraps `AchievementsResponseDataDto`.
- **Domain Layer**:
  - `AchievementDomainModel` & `AchievementsData`: Domain representations of achievement metadata, counts (`earnedCount`, `inProgressCount`, `xpEarned`), and statuses. Includes `description`, `progressPercent`, `targetValue`, `xpReward`, `coinReward`, `earnedAt`.
  - `AchievementRepository` / `AchievementRepositoryImpl`: Coordinates data fetching and maps DTOs to domain entities.
  - `GetAchievementsUseCase`: Granular UseCase providing filtered or unfiltered achievement datasets to presentation layers.
- **Dependency Injection**:
  - `AchievementModule`: Hilt module providing `AchievementApiService` via Retrofit and binding datasource and repository interfaces.

### B. Leaderboard Feature (`features/leaderboard`)
- **Remote Data Source**:
  - `LeaderboardApiService`: Targets `GET /leaderboard` supporting pagination (`page`, `limit`), `scope` (`GLOBAL`, `COUNTRY`), and optional `languageId`.
  - `LeaderboardRemoteDataSource` / `LeaderboardRemoteDataSourceImpl`: Handles safe API communication returning `LeaderboardDataDto`.
- **Domain Layer**:
  - `Leaderboard` & `LeaderboardEntry`: Domain models representing rankings, users, scores, and positions.
  - `GetLeaderboardUseCase`: UseCase supporting scope selection and pagination.
- **Presentation**:
  - Fully mapped to localized `UiText` for robust error messaging.

### C. Profile Feature Integration (`features/profile`)
- **Profile DTOs & Mappers**:
  - `ProfileSummaryDto`: Serializes both legacy summary structures and direct `achievements` and `leaderboard` arrays from the `/profile` endpoint.
  - `ProfileMapper`: Maps API lists to domain summaries (`AchievementsSummary`, `LeaderboardSummary`), computing aggregate counters when necessary.
  - `ProfileUiMapper`: Handles dynamic icon and avatar mapping with fallback defaults (`R.drawable.achievement_cup`, `R.drawable.avatar_1`), ensuring seamless rendering with Coil via `ImageWrapper`.

---

## 2. State Management (MVI Architecture)

### Achievements MVI Contract
- **State (`AchievementState`)**:
  - `isLoading: Boolean`
  - `filter: AchievementFilter`
  - `earnedCount: Int`, `inProgressCount: Int`, `xpEarned: Int`
  - `achievements: List<AchievementItem>` (including description, status, progress, target, rewards)
  - `errorMessage: UiText?`
- **Intent (`AchievementIntent`)**:
  - `LoadAchievements`: Requests current achievement data.
  - `ChangeFilter(filter: AchievementFilter)`: Updates filter and reloads data.
- **ViewModel (`AchievementViewModel`)**:
  - Manages `StateFlow<AchievementState>` and `isOnline: StateFlow<Boolean>`.
  - Maps domain model statuses to UI `AchievementItem` models and network failures to `UiText`.

### Leaderboard MVI Contract
- **State (`LeaderboardState`)**:
  - `isLoading: Boolean`, `isLoadingMore: Boolean`, `endReached: Boolean`
  - `leaderboard: Leaderboard?`
  - `scope: LeaderboardScope`, `languageId: Int?`, `currentPage: Int`
  - `errorMessage: UiText?`
- **Intent (`LeaderboardIntent`)**:
  - `LoadLeaderboard`, `LoadMore`, `ChangeScope`
- **ViewModel (`LeaderboardViewModel`)**:
  - Implements infinite pagination and scope switching.

---

## 3. Navigation 3 Keys

The feature screens are registered as type-safe Navigation 3 destinations:
- `RootNavGraph.Achievements`: Routes to `AchievementScreen(onBackClick = ...)`.
- `RootNavGraph.Leaderboard`: Routes to `LeaderboardScreen(onBack = ...)`.
- `RootNavGraph.Profile`: Routes to `ProfileScreen`, which renders preview sections for both achievements and nearby leaderboard ranks.

---

## 4. UI & Shared Components

- **`AchievementDetailBottomSheet`**: Interactive Modal Bottom Sheet providing in-depth information about an achievement upon clicking any grid card:
  - Hero icon badge with glowing background halo.
  - Status pill (`COMPLETED`, `IN PROGRESS`, `LOCKED`).
  - Full objective description.
  - Dynamic progress bar with completion percentage.
  - Reward pills showing XP (`ic_xp`) and Coins (`ic_coin`).
  - Earned date or encouragement footer.
  - Dismiss / close action.
- **`AchievementGridItem` & `AchievementCard`**: Compose cards with entry animations, status styling, and click handling.
- **`ImageWrapper`**: Standardized Coil image loader supporting both local drawable resource IDs (`Int`) and remote image URLs (`String`).
- **`OfflineAwareContent`**: Displays offline banner when network connectivity is lost.
- **`ErrorView` & `LoadingView`**: Standardized feedback views consuming `UiText` string resolution.
