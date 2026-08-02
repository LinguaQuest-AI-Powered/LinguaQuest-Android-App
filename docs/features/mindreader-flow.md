# Mind Reader Feature (Akinator Game)

## Overview
Lingo's Mind Reader is an Akinator-style mini-game where the AI tries to guess the word the user is thinking of within a specific world/category by asking up to 20 Yes/No questions. The feature integrates with the Clean Architecture domain logic of LinguaQuest.

## State Management & Architecture
- **MVI Contract**: 
    - `MindReaderState`: Central data class capturing game phase (`MindReaderPhase`), emotions, current questions, translation status, result entities, and stats.
    - `MindReaderIntent`: Triggers all user interactions including `StartGameClicked`, `AnswerClicked`, `GuessVerifiedCorrect/Incorrect`, `PopQuizAnswered`, and `StumpWordSelected`.
    - `MindReaderEffect`: SharedFlow events for one-shot UI actions such as `NavigateBack` and `PlayAudio`.
- **ViewModel**: `MindReaderViewModel` orchestrates the flow. It retains the `MindReaderGameState` (domain model) and `MindReaderDataset` internally and utilizes UseCases to advance the state, mapping domain outputs to `MindReaderState` for Compose.

## Navigation
The flow uses Jetpack Navigation 3 standards:
- **NavKey**: `RootScreen.MindReader(val worldId: Int? = null)`
- **Router**: The `NavDisplay` in `AppNavigation.kt` resolves this key to the `MindReaderScreen` composable.

## Presentation Components
The UI implements a state-swapping approach within a single `MindReaderScreen` using `Crossfade`, alternating between:
1. `PreGameLobbyContent`: Category confirmation.
2. `ActiveGameContent`: Question loop with translation and playback.
3. `LoadingGuessContent`: Simulated suspense before a reveal.
4. `GuessRevealContent`: Lingo's final guess.
5. `PopQuizContent`: Honesty verification via a translation check.
6. `AkinatorTrapContent`: Stumping the AI and forcing the user to pick their intended word.
7. `ResultContent`: Final victory or busted outcomes showcasing XP/Coin distribution.

All components utilize `LinguaQuestScreenTopBar` and `AppButton` from the shared core components.
