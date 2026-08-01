# MindReader UI Refinement

## Overview
The MindReader (Akinator-style) game feature's UI was refined to match the Figma design specifications. All 7 content screens, 2 shared components, and supporting model/string changes were updated.

## Changes Summary

### Model Layer
- **MindReaderCategory** (`domain/model/MindReaderCategory.kt`): Added `emoji: String` field
- **GetMindReaderCategoriesUseCase** (`domain/usecase/GetMindReaderCategoriesUseCase.kt`): Now derives category emoji from the first entity in each world group

### New Components
- **MindReaderAnswerButton** (`presentation/view/components/MindReaderAnswerButton.kt`): Beige/tan pill-shaped button used for game answers and quiz choices. Background `Color(0xFFEBE0D3)`, full-width, rounded pill shape.

### Updated Components
- **MindReaderSpeechBubble** (`presentation/view/components/MindReaderSpeechBubble.kt`): Warm cream background (`Color(0xFFFFF3E6)`), subtle shadow, bold uppercase text
- **CategorySelectionCard** (`presentation/view/components/CategorySelectionCard.kt`): Now shows "CURRENT CATEGORY" label with emoji icon and category name, no longer acts as dropdown trigger

### Screen Refinements

| Screen | File | Key Changes |
|--------|------|-------------|
| Lobby | `AkinatorLobbyContent.kt` | Large `displaySmall` title, speech bubble, static category card + dropdown, START GAME + Change buttons, XP+Coins in top bar |
| Active Game | `ActiveGameContent.kt` | Progress bar above gradient box with teal track, speech bubble question, translate chip with 🌐 icon, speaker icon, beige answer buttons |
| Loading | `LoadingGuessContent.kt` | Mascot in circular beige frame, large heading text, gradient card, removed CircularProgressIndicator |
| Guess Reveal | `GuessRevealContent.kt` | Large "I think it's..." heading, prominently displayed entity emoji (72sp), beige "wrong" + orange "correct" buttons side by side |
| Pop Quiz | `PopQuizContent.kt` | Speech bubble, large heading, target word in orange-bordered card, beige choice buttons |
| Stump/Trap | `AkinatorTrapContent.kt` | Speech bubble, styled dropdown with emoji items, Submit + Return to Home buttons |
| Result | `ResultContent.kt` | Contextual speech bubble (victory/busted), XP/Coins with icon badges, Play Again with replay icon, Return to Home with teal outline |

### String Resources
New strings added to `strings.xml`:
- `mind_reader_pop_quiz_speech`, `mind_reader_victory_speech`, `mind_reader_stumped_title`
- `mind_reader_return_to_home`, `mind_reader_play_again`
- `mind_reader_xp_value`, `mind_reader_coins_value`
- `mind_reader_trap_speech`, `mind_reader_busted_title`

Updated: `mind_reader_experience` → "EXPERIENCE", `mind_reader_earnings` → "EARNINGS"

## Design System Compliance
- All screens use `LinguaQuestTheme.colors` and `MaterialTheme.colorScheme`/`typography`
- No hardcoded color values except for feature-specific beige (`0xFFEBE0D3`) and cream (`0xFFFFF3E6`) that are consistent across the feature
- `AppButton`, `AppMascotGradientBox`, `AppGradientBackgroundBox`, and `LinguaQuestScreenTopBar` shared components are reused throughout
- All user-facing text uses `stringResource()` with no string literals
