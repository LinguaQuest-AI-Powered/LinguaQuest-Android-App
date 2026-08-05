# Voice Game Unit Testing Documentation

## Overview
This document describes the unit testing suite created for the **Voice Game** feature. The tests cover the Data layer (`VoiceGameRepositoryImpl`), Domain layer (`GeneratePronunciationSentenceUseCase`, `EvaluatePronunciationUseCase`), and Presentation layer (`VoiceGameViewModel`, `VoiceResultViewModel`).

## Architecture & Testing Coverage

### 1. Data Layer (`VoiceGameRepositoryImplTest`)
- **Pronunciation Evaluation**: Verifies success response mapping from `VoiceEvaluationService` into `VoiceEvaluation` domain models.
- **Language Resolution**: Tests resolving app language fallback via `UserPreferencesRepository` when `appLanguage` is null.
- **Sentence Generation**: Tests converting `GeneratedSentence` models from `PronunciationSentenceGeneratorService` into `PronunciationSentence`.
- **Error Handling**: Verifies mapping exceptions to `LinguaQuestResult.Failure(LinguaQuestDataError.CustomServerMessage)`.

### 2. Domain Layer (`GeneratePronunciationSentenceUseCaseTest` & `EvaluatePronunciationUseCaseTest`)
- **Parameter Delegation**: Ensures parameter forwarding to `VoiceGameRepository`.
- **Result Handling**: Validates `LinguaQuestResult.Success` and `LinguaQuestResult.Failure` propagation.

### 3. Presentation Layer (`VoiceGameViewModelTest` & `VoiceResultViewModelTest`)
- **State & Intent Management**: Verifies initial state, sentence generation intents (`Init`, `GenerateNewSentenceClicked`, `SkipClicked`), and TTS listening.
- **Audio Recording Lifecycle**: Tests mic permissions, starting recording, pausing/resuming recording, and preview playback.
- **Evaluation Flow**: Tests recording evaluation (`ConfirmProcessClicked`), reward calculation (10 coins on rating >= 6), wallet adjustment, and side effect emissions (`VoiceGameEffect.NavigateToResult`).
- **Error & Cancellation**: Verifies error snackbars and resetting recording state.
- **Coroutines Testing**: Uses `MainDispatcherRule` to safely mock `Dispatchers.Main` during ViewModel scope execution.

## Verification
All tests are executed via Gradle:
```bash
./gradlew testDebugUnitTest
```
