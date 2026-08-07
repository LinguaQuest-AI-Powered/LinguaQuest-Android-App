# Game Feature Updates

## Overview
This document summarizes a series of UI and UX updates made to the Game Feature (Level/Camera/Processing/Result) inside `com.iti.linguaquest.features.game`.

## Modifications

1. **Word Box Boundaries:** 
   - Added horizontal padding to the `Text` component inside the target word box in `QuestCard.kt`. This prevents the target word from touching the box borders when it is lengthy.

2. **Change Word / Hint Loading States:**
   - Modified `LevelScreen.kt` to safely disable the "Change Word" button when the hint is currently loading (`!state.isHintLoading`).

3. **Hint Bottom Sheet:**
   - Updated the hint string resource to `"Get a hint"` in `HintsBottomSheet.kt` for clearer messaging compared to the previous `"Make Sure it's well lit!"`.

4. **Hint Consumed State:**
   - The UI now passes an `isHintConsumed` state down to `QuestCard.kt`. When a hint has been successfully consumed by the user, the mascot helper text dynamically changes to `"You can change the word if you are stuck"`.
   - Tapping the mascot in this consumed state triggers the `ChangeWordClicked` intent.

5. **Camera Screen Word Box:**
   - Introduced a prominent Search (`Icons.Default.Search`) icon next to the targeted word string inside the semi-transparent black Card overlay located at the top center of `CameraPreviewContent.kt`.

6. **Evaluating Screen Word Box:**
   - Brought over the same target word Search Card design into the Game Processing View (`GameProcessingView.kt`). The `targetWord` is now securely propagated from `sharedState` in `GameProcessingScreen.kt`.

7. **Lingo Processing Animations:**
   - Expanded the list of dynamic phrases presented while the app is validating the image (`ProcessingStatusText.kt`), adding lines like `"Analyzing pixels..."`, `"Comparing with dictionary..."`, and `"Thinking hard..."`.
   - Adapted `FloatingLingo.kt` to cyclically animate between `lingo_checking_pronounciation` and `lingo_camera` images while verifying.

8. **Fail Screen Hint Button Removal:**
   - Replaced the hint `AppButton3D` with a static, non-clickable informational row in `GameFailView.kt` if the hint was not already used.

9. **Coin Icon Standardization:**
   - Enforced the standardized use of the `ic_coin` drawable (`R.drawable.ic_coin`) for coin rewards across `HintsBottomSheet.kt` and `FlyingCoinBadge.kt`, migrating away from `Icons.Default.MonetizationOn`.

10. **API Layer Nomenclature:**
    - Transitioned the `levelId` parameter variable name to `order` in API routing (e.g., `worlds/{worldId}/levels/{order}/start`), `LevelApiService.kt`, `LevelRemoteDataSource.kt`, and `LevelRepository.kt` to correctly align with backend expectations.

11. **Shared Components Enforcement:**
    - Standardized `CameraPermissionView.kt` to use the global `AppButton3D` and `AppOutlinedButton` shared components instead of basic Material buttons, ensuring a cohesive design system throughout the entire feature.

12. **Level Screen UI Enhancements:**
    - Updated the "Change Word" dialog in `LevelScreen.kt` to display the `lingo_on_coins` mascot image.
    - Simplified the Level screen app bar title to display "Level" consistently (avoiding the flash of "Level 1" during initial loading).
    - Introduced a smooth `AnimatedContent` transition to the target word text inside `QuestCard.kt` to improve the visual experience when the word is swapped.

13. **Map Routing Update:**
    - Modified `MapViewModel.kt` to properly pass the level's `order` (instead of the database `levelId`) down to the `LevelScreen`, ensuring the game engine queries the backend using the level order.
