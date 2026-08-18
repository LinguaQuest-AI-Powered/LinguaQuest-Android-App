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

## 3. Token & Quota Optimizations

To operate smoothly on free-tier API keys without hitting rate limits (15 RPM) or inflating token consumption, MindReader employs four optimization strategies:

1. **Combined Guess + Pop-Quiz Payload**:
   - When the AI makes a guess (`type: "guess"`), it generates the 3 vocabulary quiz choices inside the same response.
   - When the user confirms the guess is correct, the Pop Quiz renders instantly from memory with **0 additional network calls**.
2. **Instant Local Seed Opening Questions**:
   - Each category in `res/raw/mindreader_categories.json` includes multilingual broad partition questions.
   - Turn 1 displays instantly with **0ms latency and 0 network calls**.
   - The first API call is only made on Turn 2, providing the AI with rich initial context.
3. **Prompt Token Compression**:
   - `MindReaderPromptFactory` utilizes high-density, concise prompt schemas, reducing prompt token size by 40–50%.
4. **Smart Information-Gain Binary Deduction**:
   - The deduction prompt guides the AI to maintain an active hypothesis set and select high-entropy 50/50 partition questions that eliminate contradictions and narrow the search space rapidly.
   - When confidence reaches >= 75% (typically within 4–7 questions), the AI initiates the guess without artificial pacing delays.

---

## 4. Localization & In-Game Economy

1. **Category Localization**:
   - All 18 categories define multilingual `displayNames` (`ar`, `en`, `es`, `fr`, `de`, `it`, `pt`) in `mindreader_categories.json`.
   - Category names dynamically adapt to the user's active native language (`category.resolveDisplayName(state.nativeLanguageCode)`).
2. **Translation Economy & Loading (5 Coins)**:
   - Revealing question translations in an active game costs 5 coins (`GameCost.MIND_READER_TRANSLATION`).
   - Tapping the translate button prompts a confirmation dialog via `DialogController` (`DialogUiState`).
   - During transaction and translation creation, `LingoSpinningIcon` is displayed in both the action row chip and the translation box until the text resolves.
   - If the player has insufficient coins, an error snackbar is displayed via `SnackbarController`.
3. **Result Screen Reason Localization**:
   - All result reasons (timeout, contradiction, network loss, wrong quiz answer) use strongly typed `UiText.StringResource` tokens with complete Arabic and English localizations.

---

## 5. Presentation Layer (MVI) & UI Transitions

- **State (`MindReaderState`)**: Tracks current phase (`LOBBY`, `THINKING`, `PLAYING`, `GUESSING_LOADING`, `GUESS_REVEAL`, `POP_QUIZ`, `STUMP`, `RESULT`), balances, current question candidate, translation loading (`isTranslating`), and result info (`MindReaderResultInfo`).
- **Intent (`MindReaderIntent`)**: User interactions including `StartGameClicked`, `AnswerClicked`, `GuessVerifiedCorrect/Incorrect`, `PopQuizAnswered`, `StumpSubmitClicked`, and `TranslateClicked`.
- **Effect (`MindReaderEffect`)**: One-time side effects (`NavigateBack`, `PlayAudio`).
- **Overlay Loading Pattern**: Following the `VoiceGame` UX pattern, asynchronous phases (`THINKING` and `GUESSING_LOADING`) render `LoadingView` as a modal dialog overlay directly on top of the underlying active view (`ActiveGameContent`, `PopQuizContent`, `AkinatorTrapContent`, `GuessRevealContent`) rather than replacing the screen inside `Crossfade`. During this time, input interactions in `ActiveGameContent` are automatically disabled (`isInputEnabled = state.currentPhase == MindReaderPhase.PLAYING`).
- **Global Dialogs & Error Handling**: Leverages `DialogController` for confirmation modals and `SnackbarController` with `AiErrorMapper` for error toasts.

---

## 6. Navigation
- **NavKey**: `RootScreen.MindReader(val worldId: Int? = null)`
- **Router**: Registered in `AppNavigation.kt` rendering `MindReaderScreen`.
