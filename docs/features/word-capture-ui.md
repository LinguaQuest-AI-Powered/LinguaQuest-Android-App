# Word Capture UI (Camera Card)

## Overview
The `WordCaptureCard` is a stateless Jetpack Compose UI component designed to resemble a physical camera. It acts as the entry point for the "Camera Hunt" / Word Capture feature from the LinguaQuest `HomeScreen`. 

## Location & Integration
- **File:** `WordCaptureCard.kt`
- **Path:** `app/src/main/java/com/iti/linguaquest/features/home/presentation/view/components/WordCaptureCard.kt`
- **Integration:** Replaces the previous `VoicePractiseCard` inside `HomeContent` (`HomeScreen.kt`).

## Visual Composition
The card utilizes overlapping Compose `Box` elements and standard `RoundedCornerShape` / `CircleShape` modifiers to create its unique physical camera appearance:
1. **Camera Body:** A beige rounded rectangle with a darker drop shadow border.
2. **Top Accessories:** A ridged shutter button (top-left) and a viewfinder/flash module (top-right).
3. **Mascot:** Integrates the parrot mascot holding a camera (`R.drawable.lingo_camera_permission`).
4. **Lens Area:** Nested circular boxes with concentric strokes (Teal and Beige) displaying the target word in the center.
5. **Action Button:** A bottom primary button with a camera icon (`R.drawable.ic_camera`).

## State Management & MVI (UI Only Phase)
Currently, the component is implemented as a **Stateless Component** (`ui only`). It exposes a configuration API through default parameters:
- `questLabel`
- `worldName`
- `instruction`
- `progressText`
- `targetWord`
- `buttonText`

*Future iterations will wire this component into `HomeState` by fetching data from `GetHomeSummaryUseCase` and dispatching specific Navigation Intents (`HomeIntent.WordCaptureCardClicked`).*
