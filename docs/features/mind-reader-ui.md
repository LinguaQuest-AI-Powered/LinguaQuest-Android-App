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
- `AkinatorLoadingGuessContent.kt`: Animated/processing mascot view during AI computation.
- `AkinatorGuessRevealContent.kt`: Displays AI's guessed entity with emoji and confirmation actions.
- `AkinatorPopQuizContent.kt`: Intermediate vocabulary quiz testing the player on foreign words.
- `AkinatorTrapContent.kt`: Candidate selection when AI is stumped and requires user input.
- `AkinatorResultContent.kt`: Final game outcome screen displaying victory/loss status, rewards earned, and replay options.

## Clean Architecture Compliance
- Strict MVI: `MindReaderState`, `MindReaderIntent`, `MindReaderEffect`
- Zero inline fully qualified package names
- Zero code comments / boilerplate comments
- String resources strictly managed via `R.string.*` and `UiText`

