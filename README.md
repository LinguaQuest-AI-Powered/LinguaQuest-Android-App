# LinguaQuest 🦜

[![Android CI](https://github.com/LinguaQuest-AI-Powered/LinguaQuest-Android-App/actions/workflows/build-debug-apk.yml/badge.svg)](https://github.com/LinguaQuest-AI-Powered/LinguaQuest-Android-App/actions/workflows/build-debug-apk.yml)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Android SDK](https://img.shields.io/badge/Min%20SDK-24-3DDC84?style=flat&logo=android&logoColor=white)
![Compile SDK](https://img.shields.io/badge/Compile%20SDK-37-3DDC84?style=flat&logo=android&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVI-blue?style=flat)
![Navigation](https://img.shields.io/badge/Navigation-Jetpack%20Nav%203-orange?style=flat)
![AI Powered](https://img.shields.io/badge/AI-Gemini%20%7C%20DeepSeek%20%7C%20Llama-purple?style=flat)

**LinguaQuest** is a cutting-edge, AI-powered interactive language learning Android application. Designed around immersive gamification, real-world object recognition, bidirectional voice roleplay, and pedagogical assessment, LinguaQuest transforms language acquisition into an interactive adventure.

---

## 🌟 Primary Feature: Real-World Word Capture Game

The core flagship mechanic of LinguaQuest is the **Camera Word Capture Game**, an interactive real-world object hunt that turns physical surroundings into language learning opportunities.

```
       Map Level Node
             │
             ▼
   ┌───────────────────┐
   │    Quest Card     │  --> Displays Target Vocabulary Word (e.g., "Apple" / "Chair")
   └─────────┬─────────┘
             │
             ▼
   ┌───────────────────┐
   │ CameraX Viewfinder│  --> User searches real-world environment & captures photo
   └─────────┬─────────┘
             │
             ▼
   ┌───────────────────┐
   │ AI Vision Engine  │  --> Analyzes image pixels against multi-model AI vision classifiers
   └─────────┬─────────┘
             │
             ├──────────────────────────┐
             ▼                          ▼
     [ Match Success ]          [ Match Failure ]
     • +XP & Coins              • Mascot Hint Assistance
     • Level Node Unlocked      • Retry or Swap Word
     • Spaced Review Storage
```

### Key Highlights of the Word Capture Engine:
* **Real-World Object Recognition**: Uses **CameraX** to scan physical objects. Captured images are evaluated by our AI Vision Engine (`ItiGatewayAiClient` / Gemini / Backend API) to verify if the photograph matches the assigned target vocabulary word.
* **Interactive Quest Cards**: Built with custom `WordCaptureCard` UI components mimicking a physical camera lens, complete with target word banners, concentric camera strokes, and animated mascots.
* **Gamified Assistance System**: Integrated `HintsBottomSheet` providing hints, coin-based word swapping, and mascot guidance when users are stuck.
* **Dynamic AI Processing Feedback**: Displays real-time status indicators (`ProcessingStatusText`) with cycling animations ("Analyzing pixels...", "Comparing with dictionary...", "Thinking hard...") while evaluating captured images.
* **Gamification & Wallet Persistence**: Successful object captures award **XP** and **Coins**, which automatically persist to the remote and local database via `AdjustWalletUseCase`.

---

## 🚀 Key Features

### 📸 1. Real-World Camera Word Capture (Core Game)
* Search physical surroundings for target vocabulary words using the in-app CameraX scanner.
* Real-time multi-model AI image evaluation validating object matches.
* Instant level completion, progression unlocks, and wallet rewards.

### 🖼️ 2. Word Capture Gallery & Visual Dictionary
* Interactive visual album archiving all real-world photos captured during the Camera Word Capture game.
* Category filtering (Objects, Animals, Food, Household) with interactive word detail cards and native speech audio playback.
* Direct integration with Lock Screen Vocabulary lists for screen-off practice.

### ⚔️ 3. AI Roleplay & Boss Challenges
* Engage in real-time spoken conversations with AI bosses (e.g., barista, immigration officer).
* Powered by dual AI engines: **Gemini Live Multimodal API** (bidirectional 16kHz PCM audio streaming) and **ItiGatewayAiClient / DeepSeek V3.2**.
* 3-dimensional pedagogical scoring assessing **Fluency**, **Grammar**, and **Vocabulary**.
* Built-in `TranscriptSanitizer` preventing native language gaming through script ratio verification and ASR artifact cleanup.

### 🎤 4. Voice Game & Pronunciation Evaluation
* Interactive speech-driven exercises evaluating spoken fluency and sentence construction.
* Instant feedback on pronunciation accuracy using specialized speech models.

### 🔮 5. Mind Reader AI
* Conversational AI guessing game that engages learners in target-language question-and-answer scenarios.

### 🔒 6. Lock Screen Vocabulary Notifications
* Screen-off vocabulary learning system displaying word notifications upon locking the device.
* Features smart retry scheduling (`VocabularyScreenOffReceiver`) and Android 14+ full-screen intent support (`USE_FULL_SCREEN_INTENT`).

### 🏆 7. Achievements & Global Leaderboard
* Real-time global player rankings, weekly leaderboards, customizable app mascot avatars, and unlockable achievement badges.

### 🗺️ 8. Interactive Map & World Progression
* Multi-world adventure map with unlockable level nodes, stage objectives, and star ratings.

### 🎴 9. Spaced Vocabulary Review
* Flashcard review system storing captured words and pronunciations for long-term memory retention.

### 🔔 10. Push Notifications & FCM Integration
* Firebase Cloud Messaging (FCM) integration for personalized study reminders, daily mission prompts, and streak loss alerts.

---

## 🏗️ Architecture & Technology Stack

LinguaQuest follows **Clean Architecture** principles decoupled into domain, data, and presentation layers, combined with **MVI (Model-View-Intent)** state management and **Jetpack Navigation 3**.

```
app/src/main/java/com/iti/linguaquest/
├── core/
│   ├── ai/               <-- Centralized provider-agnostic AiClient & Roleplay services
│   ├── audio/            <-- AudioRecorder, AudioPlayer (PCM 16kHz stream pipeline)
│   ├── database/         <-- Room Database entities & DAOs
│   ├── di/               <-- Hilt Dependency Injection modules
│   ├── navigation/       <-- Jetpack Navigation 3 NavKey definitions (Screens.kt)
│   ├── network/          <-- Retrofit & Ktor networking components
│   ├── sharedComponents/ <-- Reusable Compose design components (AppButton3D, etc.)
│   └── theme/            <-- LinguaQuest custom design tokens & color schemes
└── features/
    ├── game/             <-- Core Camera Word Capture Game engine & views
    ├── roleplay/         <-- AI Voice Roleplay & Boss Challenge module
    ├── home/             <-- Dashboard, WordCaptureCard, quest summary
    ├── map/              <-- World progression map
    ├── voicegame/        <-- Speech practice feature
    ├── mindreader/       <-- Mind Reader AI guessing game
    ├── lockscreen/       <-- Lock screen notification settings & receivers
    ├── leaderboard/      <-- Global leaderboard & rankings
    └── ...               <-- Additional feature modules (auth, profile, review, etc.)
```

### 🧩 Architectural Highlights

* **MVI Pattern**: Continuous UI state exposed via `StateFlow<T>`, one-shot UI effects handled via `SharedFlow<T>`, and intent handling consolidated in `onIntent(intent)`.
* **Type-Safe Jetpack Navigation 3**: Driven by a developer-owned back stack (`rememberNavBackStack`) using `@Serializable` sealed interfaces extending `NavKey`.
* **Provider-Agnostic AI Client (`AiClient`)**: Single unified interface supporting **ItiGatewayAiClient** (DeepSeek V3.2, Llama 3.3 70B, Voxtral 24B), **GeminiAiClient**, and **FirebaseAiClient** with transparent failover and sticky caching.
* **Audio Engineering**: Circular 250ms pre-buffer in `AudioRecorder` preventing syllable clipping, `VOICE_RECOGNITION` hardware tuning, and ~1s trailing silence tail padding for reliable Gemini Voice Activity Detection (VAD).

---

## 🛠️ Tech Stack & Dependencies

| Category | Technology / Library |
| :--- | :--- |
| **Language & Core** | Kotlin `2.0.21`, JDK 17, Android SDK 37 (Min SDK 24) |
| **UI & Styling** | Jetpack Compose (BOM), Material 3, Lottie Compose, Konfetti, Coil |
| **Navigation** | Jetpack Navigation 3 (`androidx.navigation3`) |
| **Dependency Injection** | Hilt (`com.google.dagger:hilt-android`) + KSP |
| **Local Data & Cache** | Room Database, Encrypted DataStore Preferences |
| **Networking** | Retrofit 2, Ktor Client (WebSocket & Content Negotiation), OkHttp |
| **AI Providers** | ITI AI Student Gateway (DeepSeek, Llama, Voxtral), Google Gemini REST & Live WebSocket, Firebase Vertex AI |
| **Hardware & Audio** | CameraX (`camera-camera2`, `camera-lifecycle`), `AudioRecord`, `AudioTrack` |
| **Firebase Services** | Firebase Auth, Firebase Cloud Messaging (FCM), Google Sign-In |
| **Logging & Diagnostics** | Timber Logging, LeakCanary (Debug builds) |
| **Testing Framework** | JUnit 4, MockK, Turbine, `kotlinx-coroutines-test` |

---

## ⚙️ Environment Setup & Configuration

### Prerequisites
* **Android Studio**: Ladybug (2024.2.1) or newer
* **JDK**: Version 17
* **Android SDK**: API Level 34+ (Compile SDK 37)

### 1. Key Configuration (`local.properties`)
Create or edit `local.properties` in the root directory and specify your API endpoints and access keys:

```properties
# Base Backend API URL
BASE_URL="https://your-backend-api.com/"

# ITI AI Gateway Configuration
AI_BASE_URL="http://apiaccess.iti.net.eg/api/v1/student/"
AI_KEY="your_iti_ai_gateway_key"

# Gemini API Key (Direct REST & Live Multimodal Audio)
GEMINI_API_KEY="your_gemini_api_key"
```

### 2. Firebase Setup (`google-services.json`)
Download your `google-services.json` file from the Firebase Console and place it in the `app/` directory:
```
app/google-services.json
```

---

## 📦 Building & Running

### Debug Build
Build and install the debug APK onto a connected Android device or emulator:
```bash
./gradlew assembleDebug
```

### Running Unit Tests
Execute the comprehensive unit test suite:
```bash
./gradlew testDebugUnitTest
```

---

## 🔄 CI/CD Pipeline

LinguaQuest features automated Continuous Integration via GitHub Actions:
* **Workflow**: `.github/workflows/build-debug-apk.yml`
* **Trigger**: Pushes and Pull Requests to main branches.
* **Artifacts**: Automatically compiles the debug APK, executes static code checks, and publishes build artifacts.

---

## 📜 License

This project is developed as part of the ITI Final Project under the **LinguaQuest** team. All rights reserved.
