# Roleplay Feature Unit Testing Documentation

## Overview
This document outlines the unit testing suite implemented for the **Roleplay (Boss Stage)** feature. The tests cover the Data layer (`RoleplayRepositoryImpl`, `ScenarioRepositoryImpl`), Domain layer (UseCases & `PromptFactory`), and Presentation layer (`RoleplayViewModel`, `RoleplayListViewModel`, `RoleplayErrorMapper`).

## Architecture & Testing Coverage

### 1. Presentation Layer
- **`RoleplayViewModelTest`**:
  - Validates MVI intent processing (`LoadBossLobby`, `StartBossStageClicked`, `RecordClicked`, `StopRecordingClicked`, `FinishStageClicked`, `RetryStageClicked`, `ReturnHomeClicked`, `AdvanceToNextWorldClicked`).
  - Verifies continuous UI state updates (`targetLanguage`, `wallet`, `isOnline`, `isConnected`, `isUserSpeaking`, `isAiSpeaking`, `isAiThinking`, `isEvaluating`).
  - Tests Live events handling (`RoleplayLiveEvent.Transcription`, `TurnComplete`, `AudioChunk`, `Error`).
  - Tests stage evaluation, reward dispatch via `AdjustWalletUseCase`, and navigation side effects (`RoleplayEffect.NavigateToHome`, `ShowSnackbarAndNavigateBack`).
- **`RoleplayListViewModelTest`**:
  - Tests scenario loading lifecycle, loading state updates, and graceful error handling on failure.
- **`RoleplayErrorMapperTest`**:
  - Tests mapping network errors, HTTP 429 quota exceptions, GOAWAY frames, and connection dropouts to localized `UiText` string resources.

### 2. Domain Layer
- **`EvaluateBossStageUseCaseTest`**:
  - Verifies minimum target language percentage thresholds, speech presence checks, score clamping, star award calculations (1-3 stars), XP/Coin reward tiers, and error propagation.
- **`PromptFactoryTest`**:
  - Tests formatting of system prompt templates, task objectives, language immersion rules, and evaluation schema constraints.
- **UseCase Delegation Tests**:
  - `ConnectToBossStageUseCaseTest`, `DisconnectRoleplayUseCaseTest`, `GetBossScenariosUseCaseTest`, `ObserveLiveEventsUseCaseTest`, `StartMicrophoneUseCaseTest`, `StopMicrophoneUseCaseTest`.

### 3. Data Layer
- **`RoleplayRepositoryImplTest`**:
  - Tests WebSocket live session orchestration, audio recorder and player lifecycle integration, microphone muting/resuming, and AI boss stage evaluation via Gemini.
- **`ScenarioRepositoryImplTest`**:
  - Tests JSON parsing and mapping from `ScenarioDto` to `BossScenario` domain entities.

## Verification
All unit tests and builds can be executed via Gradle:
```bash
./gradlew testDebugUnitTest --tests "com.iti.linguaquest.features.roleplay.*"
./gradlew assembleDebug
```
