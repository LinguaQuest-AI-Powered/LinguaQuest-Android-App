# MindReader UI Refinement & Modularization

## Overview
The MindReader (Akinator-style) game feature presentation layer was modularized into granular, reusable composables and fully theme-integrated to comply with LinguaQuest's design system standards.

## Theme Integration
- **`LinguaQuestColors.MindReaderBeige`**: Semantic token for game answer buttons, dropdown backgrounds, category cards, and mascot frames (maps to `AppColors.MindReaderBeige` / `DarkMindReaderBeige`).
- **`LinguaQuestColors.MindReaderCream`**: Semantic token for speech bubbles, action chips, and speaker icon backgrounds (maps to `AppColors.MindReaderCream` / `DarkMindReaderCream`).
- Zero hardcoded hex colors across all feature files.

## Component Architecture

All UI components are modularized under `com.iti.linguaquest.features.mindreader.presentation.view.components`:

| Component | Responsibility |
|-----------|----------------|
| `CategorySelectionCard` | Displays the currently selected category with emoji and title |
| `MindReaderAnswerButton` | Themed pill button for user responses and quiz choices |
| `MindReaderSpeechBubble` | Elevated speech bubble with rounded corners and themed cream background |
| `MindReaderProgressBar` | Progress indicator tracking question index vs max questions |
| `MindReaderActionRow` | Quick-action row containing translation toggle chip and speaker TTS trigger |
| `MindReaderCategoryDropdown` | Exposed dropdown menu for selecting game categories |
| `MindReaderEntityDropdown` | Dropdown for choosing candidate entities during the stump/trap phase |
| `MindReaderRewardRow` | Experience (XP) and earnings (coins) summary badges |
| `MindReaderQuizWordCard` | Orange-bordered card highlighting the target quiz word |

Shared components reused from `core/sharedComponents`:
- `AppDialog`: Used for pre-game start confirmation in Lobby.
- `AppButton`: Used for primary actions, secondary actions, and navigation.
- `AppMascotGradientBox`: Standard container for mascot headers.
- `AppGradientBackgroundBox`: Gradient cards for loading states.
- `LinguaQuestScreenTopBar`: Top bar with back navigation, XP count, and coin balance.

## Screen Structure

All content screens live under `com.iti.linguaquest.features.mindreader.presentation.view.contents`:

- `AkinatorLobbyContent.kt`: Initial screen with category selection, start game button, and confirm dialog.
- `AkinatorActiveGameContent.kt`: Active game loop with progress, mascot speech, translate/TTS actions, and 5 response buttons.
- `AkinatorLoadingGuessContent.kt`: Animated/processing mascot view with popping bounce scale/bob animation and animated bouncing dots indicator.
- `AkinatorGuessRevealContent.kt`: Displays AI's guessed entity with emoji and confirmation actions.
- `AkinatorPopQuizContent.kt`: Intermediate vocabulary quiz testing the player on foreign words.
- `AkinatorTrapContent.kt`: Candidate selection when AI is stumped and requires user input.
- `AkinatorResultContent.kt`: Final game outcome screen displaying victory/loss status with `KonfettiView` celebration effects, `LocalSoundPlayer` sound effects (`AppSound.SUCCESS` / `AppSound.FAIL`), rewards earned, and replay options.

## Animations & Sound Effects
- **Victory Celebration**: `nl.dionsegijn.konfetti.compose.KonfettiView` triggered upon victory (`resultInfo.isVictory == true`) with themed particle bursts.
- **Audio Feedback**: `LocalSoundPlayer.current` plays `AppSound.SUCCESS` on victory and `AppSound.FAIL` on defeat.
- **Loading Mascot Popping**: Continuous `rememberInfiniteTransition` driving `scale` bounce (0.92f - 1.08f) and vertical bobbing on `lingo_mind_processing`, accompanied by `LingoBouncingDots`.

## Wallet & Economy Integration
- **Persistent Balance Tracking**: `GetWalletUseCase` is collected in `MindReaderViewModel` to automatically sync real-time `xp` and `coins` balances into `MindReaderState`, displayed across all sub-screens via `LinguaQuestScreenTopBar`.
- **Victory Rewards**: `AdjustWalletUseCase` awards the calculated XP and Coins on game victory.
- **In-game Hints**: `AdjustWalletUseCase` deducts the configured coin cost when translating questions, with warning notifications dispatched via `SnackbarController` if balances are insufficient.

## Dynamic Language Localization & Translation
- **Native Language Resolution**: `ResolveMindReaderNativeLanguageCodeUseCase` resolves the user's native language code (`"ar"`, `"en"`, etc.) from `UserPreferencesRepository` and `AuthRepository`.
- **Target Language Resolution**: `ResolveMindReaderTargetLanguageCodeUseCase` resolves the language the player is currently learning.
- **Active Game Questioning & Responses**: The question is presented in the target language (`state.targetLanguageCode`), while the 5 answer buttons (Yes, No, Sometimes, Probably Not, Don't Know) dynamically resolve into the user's native language via `MindReaderAnswerOption.resolveLabel(state.nativeLanguageCode)`.
- **Question Translation**: Tapping the Translate action reveals the translated question in the player's native language (`state.nativeLanguageCode`).
- **Pop Quiz Integration**: Tests vocabulary by prompting the player with the entity word in their native language (`state.nativeLanguageCode`) on `MindReaderQuizWordCard` and displaying multiple-choice options in their target learning language (`state.targetLanguageCode`).
- **Comprehensive Multilingual Resources**: All UI strings are localized across both default English (`values/strings.xml`) and Arabic (`values-ar/strings.xml`).

## Clean Architecture Compliance
- Strict MVI: `MindReaderState`, `MindReaderIntent`, `MindReaderEffect`
- Zero inline fully qualified package names
- Zero code comments / boilerplate comments
- String resources strictly managed via `R.string.*` and `UiText`




