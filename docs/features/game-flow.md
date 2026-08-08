# Game Flow Feature

## Overview
The Game Flow manages the core gameplay loop of LinguaQuest, primarily taking place in the LevelScreen and camera flow. The feature was recently optimized so that when a level is navigated to from the Map or Home screen, the targetWord is passed directly through the navigation arguments to avoid fetching it anew from the backend.

## Architecture
- **MVI Architecture**:
  - `LevelState`: Tracks `worldId`, `levelId`, `levelOrder`, `wordToGuess`, hint loading state, coins, etc.
  - `LevelIntent`: Captures user actions.
  - `LevelEffect`: Captures one-time effects like navigation to the camera screen.
  
## State Management
- `LevelViewModel` handles core logic. It has an optimized `loadLevelDetails` function that accepts an optional `targetWord: String?`. If the `targetWord` is passed, it directly assigns it to the `LevelState` without calling the `startLevelUseCase`.

## Navigation 3 Keys
- `RootScreen.GameFlow`: Takes `(val worldId: Int, val levelId: Int, val levelOrder: Int, val targetWord: String? = null)`.
- Hosted by `GameFlowHost`, which provides a nested navigation container using a separate back stack for intra-game screens (`CameraScreen`, `ProcessingScreen`, `ResultScreen`).
