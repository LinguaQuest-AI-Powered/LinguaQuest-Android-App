# App Tour & Contextual Tutorials

This document provides a high-level overview of the App Tour/Contextual Spotlight Tutorial system.

## Overview
The tutorial system is built entirely on custom Canvas drawing and dynamic coordinate registration in Jetpack Compose to avoid dependency version conflicts and ensure stable overlay rendering.

## Architecture
- **State Management**: managed via `TutorialManager` (Hilt `@Singleton`), which publishes flow updates to the UI.
- **Coordination**: targets register their bounds in the layout context using `Modifier.tutorialTarget(stepId, manager)`.
- **Active Tour (Home Features)**: The app tour highlights the following home features sequentially:
  1. `tutorial_top_bar_coins` (Coins): Tracks the user's earned coins.
  2. `tutorial_top_bar_xp` (XP): Tracks the user's experience points.
  3. `tutorial_top_bar_notifications` (Bell Icon): Points to vocabulary reminders and achievements.
  4. `tutorial_language_progress` (LanguageProgressCard): Shows the user's selected language, level, learning progress, and streak.
  5. `tutorial_word_capture` (WordCaptureCard): Directs the user to resume their current lesson.
  6. `tutorial_world_list` (ExploreWorldsSection): Lists available learning worlds.
  7. `tutorial_language_button` (FAB): Lets the user switch languages or add new ones.
  8. `tutorial_daily_mission` (FAB): Points to the daily mission page/rewards.
- **Active Tour (Gallery Features)**: A contextual tour highlighting:
  1. `gallery_tab_game_captures` (Game Captures): Reviews words successfully captured in-game.
  2. `gallery_tab_my_journal` (My Journal): Vocabulary vault containing words ready for review.
  The `GalleryScreen` automatically monitors the tutorial step index to programmatically switch active tabs when transitioning through this tour.
- **Navigation Integration**: Tab switching effect is bypassed since the tours run contextually within their respective screens.
- **Persistence**: completed tours are persisted in DataStore via `TutorialPreferences` to avoid displaying them repeatedly.

## Adding a New Tour
1. Define the tour and steps in `TutorialManager.start[Feature]Tour()`.
2. Register the respective UI elements with `Modifier.tutorialTarget("target_id", manager)`.
3. Call `manager.startTour(tour)` to initiate the tutorial flow contextually.

