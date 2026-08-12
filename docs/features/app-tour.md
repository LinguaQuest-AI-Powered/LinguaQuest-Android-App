# App Tour & Contextual Tutorials

This document provides a high-level overview of the App Tour/Contextual Spotlight Tutorial system.

## Overview
The tutorial system is built entirely on custom Canvas drawing and dynamic coordinate registration in Jetpack Compose to avoid dependency version conflicts and ensure stable overlay rendering.

## Architecture
- **State Management**: managed via `TutorialManager` (Hilt `@Singleton`), which publishes flow updates to the UI.
- **Coordination**: targets register their bounds in the root layout context using `Modifier.tutorialTarget(stepId, manager)`.
- **Navigation Integration**: when the tour transitions between steps, `TutorialManager` triggers `TutorialEffect.RequestTabSwitch` to automatically transition the bottom navigation tabs.
- **Persistence**: completed tours are persisted in DataStore via `TutorialPreferences` to avoid displaying them repeatedly.

## Adding a New Tour
1. Define the tour and steps in `TutorialManager.start[Feature]Tour()`.
2. Register the respective UI elements with `Modifier.tutorialTarget("target_id", manager)`.
3. Call `manager.startTour(tour)` to initiate the tutorial flow contextually.
