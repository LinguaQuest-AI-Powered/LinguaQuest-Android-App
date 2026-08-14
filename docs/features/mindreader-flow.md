# Mind Reader Feature Architecture & Game Flow

## 1. Overview
**Lingo's Mind Reader** is an AI-powered, 20-questions Akinator-style vocabulary game where the player thinks of a word in a specific category (e.g., Kitchen, Animals, School), and the mascot (Lingo) attempts to guess it through adaptive yes/no questions in the target language.

The feature is structured according to LinguaQuest's Clean Architecture & MVI standards, with its Data and Domain layers architecturally aligned with the Roleplay feature.

---

## 2. Layer Separation & Structure

```
features/mindreader/
├── data/
│   ├── datasource/
│   │   ├── local/
│   │   │   ├── MindReaderLocalDataSource.kt
│   │   │   └── MindReaderLocalDataSourceImpl.kt
│   │   └── remote/
│   │       ├── MindReaderRemoteDataSource.kt
│   │       └── GeminiMindReaderService.kt
│   ├── dto/
│   │   └── MindReaderDtos.kt
│   └── repository/
│       └── MindReaderRepositoryImpl.kt
├── di/
│   └── MindReaderModule.kt
├── domain/
│   ├── model/
│   │   ├── MindReaderCategory.kt
│   │   └── MindReaderModels.kt
│   ├── prompt/
│   │   └── MindReaderPromptFactory.kt
│   ├── repository/
│   │   └── MindReaderRepository.kt
│   └── usecase/
│       ├── BuildMindReaderPopQuizQuestionUseCase.kt
│       ├── CreateMindReaderSessionUseCase.kt
│       ├── GetMindReaderCategoriesUseCase.kt
│       ├── GetMindReaderNextTurnUseCase.kt
│       ├── ResolveMindReaderNativeLanguageCodeUseCase.kt
│       ├── ResolveMindReaderRewardUseCase.kt
│       ├── ResolveMindReaderTargetLanguageCodeUseCase.kt
│       ├── StartMindReaderGameUseCase.kt
│       ├── SubmitMindReaderAnswerUseCase.kt
│       └── VerifyMindReaderHonestyUseCase.kt
└── presentation/
    ├── contract/
    │   ├── MindReaderEffect.kt
    │   ├── MindReaderIntent.kt
    │   └── MindReaderState.kt
    ├── view/
    │   ├── components/
    │   ├── contents/
    │   └── MindReaderScreen.kt
    └── viewmodel/
        └── MindReaderViewModel.kt
```

---

## 3. Data Layer

### Data Sources
- **`MindReaderLocalDataSource` / `MindReaderLocalDataSourceImpl`**:
  - Loads categories from `res/raw/mindreader_categories.json`.
  - Loads game configuration (turns limit, translation costs, rewards) from `res/raw/game_config.json`.
- **`MindReaderRemoteDataSource` / `GeminiMindReaderService`**:
  - Direct integration with `GeminiRestClient` using structured JSON generation.
  - Handles turn generation, vocabulary pop-quiz distractors, and anti-cheat honesty checks.

### DTOs
- `CategoryDto`: Category metadata (`id`, `displayName`, `emoji`).
- `MindReaderNextTurnDto`: Next question or guess response.
- `MindReaderQuizDto` & `MindReaderQuizChoiceDto`: Vocabulary quiz choices.
- `MindReaderHonestyDto`: Anti-cheat verification result (`isHonest`, `explanation`).

---

## 4. Domain Layer

### Models
- `MindReaderCategory`: Category representation.
- `MindReaderGameConfig`: Configuration tokens for max questions, rewards, and translation costs.
- `MindReaderGameState`: Immutable game state with turns history, question counter, asked attributes, and pending guess.
- `MindReaderAiNextTurn`: Next action from AI (`Question`, `Guess`, or `Error`).
- `MindReaderResult`: Final game resolution (`Victory`, `Busted`, `PopQuiz`, `Stump`, `Timeout`, `Playing`).

### Prompt Factory
- **`MindReaderPromptFactory`**: Centralizes all generative AI prompt engineering for:
  - Binary-split adaptive property questioning (`createNextTurnPrompt`).
  - 3-choice vocabulary pop quiz generation (`createQuizChoicesPrompt`).
  - Historical transcript honesty verification (`createHonestyVerificationPrompt`).

### Use Cases
- `GetMindReaderCategoriesUseCase`: Retrieves categories from repository.
- `StartMindReaderGameUseCase`: Initializes game launch config, language codes, and session state.
- `GetMindReaderNextTurnUseCase`: Orchestrates next turn determination via repository.
- `SubmitMindReaderAnswerUseCase`: Pure state transformer updating `MindReaderGameState`.
- `BuildMindReaderPopQuizQuestionUseCase`: Prepares 3-choice translation check upon correct AI guess.
- `VerifyMindReaderHonestyUseCase`: Calls AI honesty verification against turn history.
- `ResolveMindReaderRewardUseCase`: Evaluates outcome (Victory vs Busted) and calculates rewards.

---

## 5. Presentation Layer (MVI)

- **State (`MindReaderState`)**: Tracks current phase (`LOBBY`, `THINKING`, `PLAYING`, `GUESSING_LOADING`, `GUESS_REVEAL`, `POP_QUIZ`, `STUMP`, `RESULT`), balances, current question candidate, and result info.
- **Intent (`MindReaderIntent`)**: User interactions including `StartGameClicked`, `AnswerClicked`, `GuessVerifiedCorrect/Incorrect`, `PopQuizAnswered`, `StumpSubmitClicked`, and `TranslateClicked`.
- **Effect (`MindReaderEffect`)**: One-time side effects (`NavigateBack`, `PlayAudio`, `ShowToast`).
- **Global Error Handling**: Uses the centralized [`AiErrorMapper.kt`](file:///p:/Codes/AndroidStudioProjects/0_ITI/LinguaQuest/app/src/main/java/com/iti/linguaquest/core/sharedComponents/text/AiErrorMapper.kt) for translating 429 quota limits, network timeouts, and Gemini errors into localized `UiText` tokens.

---

## 6. Navigation
- **NavKey**: `RootScreen.MindReader(val worldId: Int? = null)`
- **Router**: Registered in `AppNavigation.kt` rendering `MindReaderScreen`.
