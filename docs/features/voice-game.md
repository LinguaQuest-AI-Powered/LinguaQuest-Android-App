# Voice Game Feature Documentation

The Voice Game feature allows users to practice their pronunciation by reading a generated target sentence aloud. The speech is recorded, converted into WAV format, and evaluated by Gemini AI to grade correctness and provide supportive coaching tips.

---

## 1. Architectural Design & State Management (MVI)

The feature follows Clean Architecture and a strict Model-View-Intent (MVI) flow:
*   **State (`VoiceGameState`)**: Manages the loading state, active sentence and phonetics, current phase (`IDLE`, `RECORDING`, `EVALUATING`), elapsed recording seconds, and preview confirmation states.
*   **Intent (`VoiceGameIntent`)**: centralizes all user actions:
    *   `Init`: Initializes or resumes the game phase.
    *   `GenerateNewSentenceClicked`: Requests the AI model to generate a new target practice sentence.
    *   `ListenClicked`: Invokes Text-To-Speech (TTS) to read the sentence.
    *   `RecordClicked`: Checks permissions and starts the active audio capture session.
    *   `DoneClicked`: Completes the recording, stopping the capture stream and showing the confirmation dialog.
    *   `CancelRecordingClicked` / `DiscardClicked`: Discards the current recording and returns to the idle state.
    *   `ConfirmProcessClicked`: Submits the recorded audio to the evaluation service.
*   **Effect (`VoiceGameEffect`)**:
    *   `RequestMicPermission`: Dispatched to request system MIC access.
    *   `NavigateToResult`: Navigates to the evaluation outcome screen upon grading.

---

## 2. Navigation Structure

*   The feature uses the Jetpack Navigation 3 state-backed stack.
*   Routes are strongly typed and defined under `Screens.kt`.
*   The transition to the evaluation result screen maps to `VoiceResultScreen` passing `VoiceResultUi` data.

---

## 3. UI Components

*   **[PushToTalkButton](file:///d:/ITI/final/LinguaQuest-Android-App/app/src/main/java/com/iti/linguaquest/core/sharedComponents/PushToTalkButton.kt)**: Shared component located in `core/sharedComponents/` that handles the press-and-hold gestures for starting and stopping mic recordings.
*   **[VoiceGameScreen](file:///d:/ITI/final/LinguaQuest-Android-App/app/src/main/java/com/iti/linguaquest/features/voicegame/presentation/view/VoiceGameScreen.kt)**: The main entry point rendering the Mascot, Target Sentence, Listen Action, and hold-to-record layout.

---

## 4. Resource & Thread Safety

*   **Audio Recording**: Managed by `AudioRecorderController`. The recording loop operates on `Dispatchers.IO` and continuously reads PCM data into a buffer.
*   **Deadlock Prevention**: To avoid blocking the UI thread and causing system Watchdog crashes, the controller stops the active `AudioRecord` instance *prior* to cancelling or joining the background coroutine job. This unblocks the blocking Java `read()` call immediately, releasing resource handles cleanly.
