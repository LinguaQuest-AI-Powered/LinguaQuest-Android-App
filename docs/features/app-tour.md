# App Tour & Contextual Tutorials

This document provides a high-level overview of the App Tour/Contextual Spotlight Tutorial system.

## Architecture

The tutorial system follows Clean Architecture principles:

- **Domain Layer**:
  - `TutorialManager`: Hilt `@Singleton` managing tutorial progression and active tour states via a centralized `TutorialIntent` processor.
  - `TourRegistry`: Standalone registry containing step and resource definitions for all app tours.
  - `IsTourCompletedUseCase`, `SetTourCompletedUseCase`, `ResetAllToursUseCase`: Single-responsibility UseCases wrapping tutorial repository contracts to enforce Clean Architecture boundaries.
  - `TutorialRepository`: Interface defining data persistence contracts (`isTourCompleted`, `setTourCompleted`, `resetAllTours`).
  - `TourId`: Type-safe enum identifying all supported tours (`APP_TOUR`, `GALLERY_TOUR`, `LINGOS_TOUR`, `PROFILE_TOUR`).
  - `TargetBounds`: Pure Kotlin domain model representing target UI coordinates `(left, top, width, height)` without Compose or Android dependencies.
  - `TutorialIntent`, `TutorialState` & `TutorialEffect`: MVI state, intent, and effect contracts under `domain.model`.

- **Data Layer**:
  - `TutorialRepositoryImpl`: Implements `TutorialRepository` using DataStore preferences.

- **Presentation Layer**:
  - `LocalTutorialManager`: `CompositionLocal` provided at `MainScreen` level to eliminate prop-drilling.
  - `TutorialOverlayScreen`: Reads `LocalTutorialManager.current` and delegates rendering to `TutorialOverlayContent`.
  - `TutorialOverlayContent`: Hoisted Composable accepting `TutorialState`, `onNext`, and `onSkip` callbacks.
  - `Modifier.tutorialTarget(stepId)`: Composable extension registering element layout bounds using `LocalTutorialManager.current`.

## Supported Tours

- **App Tour (Home Features)**: Highlights coins, XP, notifications bell, language progress card, word capture card, world list, language FAB, and daily mission FAB.
- **Gallery Tour**: Highlights Game Captures tab and My Words tab.
- **Lingos Tour**: Highlights Voice Practice, Roleplay, and Mind Reader cards.
- **Profile Tour**: Contextually highlights profile header, stats grid, settings row, achievements, and leaderboard.

## Adding a New Tour

1. Add a new `TourId` entry in `TourId.kt`.
2. Define the step sequence in `TourRegistry.kt`.
3. Register UI elements using `Modifier.tutorialTarget("target_id")`.
4. Trigger the tour using `tutorialManager.onIntent(TutorialIntent.StartTour(tourRegistry.myTour()))`.
