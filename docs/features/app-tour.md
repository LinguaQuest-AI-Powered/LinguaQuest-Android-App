# App Tour & Contextual Tutorials

This document provides a high-level overview of the App Tour/Contextual Spotlight Tutorial system.

## Overview
The tutorial system is built entirely on custom Canvas drawing and dynamic coordinate registration in Jetpack Compose to avoid dependency version conflicts and ensure stable overlay rendering.

## Architecture
- **State Management**: managed via `TutorialManager` (Hilt `@Singleton`), which publishes flow updates to the UI.
- **Coordination**: targets register their bounds in the layout context using `Modifier.tutorialTarget(stepId, manager)`.
- **Active Tour (Home Features)**: The app tour highlights the following home features sequentially:
  1. `tutorial_language_progress` (LanguageProgressCard): Shows the user's selected language, level, learning progress, and streak.
  2. `tutorial_word_capture` (WordCaptureCard): Directs the user to resume their current lesson.
  3. `tutorial_world_list` (ExploreWorldsSection): Lists available learning worlds.
  4. `tutorial_language_button` (FAB): Lets the user switch languages or add new ones.
  5. `tutorial_daily_mission` (FAB): Points to the daily mission page/rewards.
- **Navigation Integration**: Tab switching effect is bypassed since the tour runs entirely on the `HomeScreen`.
- **Persistence**: completed tours are persisted in DataStore via `TutorialPreferences` to avoid displaying them repeatedly.

## Adding a New Tour
1. Define the tour and steps in `TutorialManager.start[Feature]Tour()`.
2. Register the respective UI elements with `Modifier.tutorialTarget("target_id", manager)`.
3. Call `manager.startTour(tour)` to initiate the tutorial flow contextually.

