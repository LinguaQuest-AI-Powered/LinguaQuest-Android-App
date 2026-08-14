# Centralized AI Client Architecture

## 1. Overview
LinguaQuest utilizes a centralized, provider-agnostic **`AiClient`** interface across all generative AI features (Review, Lock Screen vocabulary, Voice Game pronunciation & sentence generation, Roleplay boss stage evaluations, and Mind Reader).

This architecture decouples the app's business logic from specific AI vendors (Gemini, Firebase, DeepSeek, OpenAI), allowing provider swapping with a single line of code in Hilt DI.

---

## 2. Architecture & Layer Structure

```
core/ai/
├── client/
│   ├── AiClient.kt           <-- Provider-agnostic interface
│   ├── GeminiAiClient.kt     <-- Implementation 1: Direct Gemini REST + Adaptive Model Caching
│   └── FirebaseAiClient.kt   <-- Implementation 2: Firebase GoogleAI SDK (Firebase.ai)
├── di/
│   └── AiModule.kt           <-- Hilt binding for @Singleton AiClient
└── network/
    ├── GeminiApiService.kt   <-- Retrofit REST API interface
    └── model/                <-- Request/Response DTOs
```

---

## 3. Interface Definition (`AiClient`)

```kotlin
interface AiClient {
    suspend fun generateText(prompt: String, temperature: Float = 0.7f): String?
    suspend fun generateJson(prompt: String, temperature: Float = 0.1f): String?
    suspend fun generateFromAudio(
        prompt: String,
        audioBytes: ByteArray,
        mimeType: String = "audio/wav",
        temperature: Float = 0.1f
    ): String?
}
```

---

## 4. Implementations

### A. `GeminiAiClient` (Primary Active Provider)
- Connects directly to Google's Generative Language REST API via Retrofit (`GeminiApiService`).
- **Sticky Active Model Caching**: Caches the last successful working model in memory to eliminate fallback latency.
- **Permanent Model Blacklisting**: Blacklists deprecated/non-existent models (404/400) so they are never retried during the session.
- **Model Priority Queue**:
  1. `gemini-flash-latest` (Auto-routes to the newest stable Flash version)
  2. `gemini-2.5-flash`
  3. `gemini-2.5-flash-lite`
  4. `gemini-pro-latest`
  5. `gemini-2.0-flash`

### B. `FirebaseAiClient` (Firebase GoogleAI SDK Provider)
- Uses `com.google.firebase:firebase-vertexai` / `Firebase.ai` SDK.
- Available as a drop-in replacement via Hilt binding.

---

## 5. Dependency Injection (`AiModule`)

To toggle between AI providers (or introduce a future `DeepSeekAiClient`), simply change the `@Binds` implementation in `AiModule.kt`:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class AiModule {

    @Binds
    @Singleton
    abstract fun bindAiClient(impl: GeminiAiClient): AiClient
}
```

---

## 6. Consuming Features

All features inject `AiClient` directly:
- **Review**: `ReviewRemoteDataSourceImpl` (`aiClient.generateJson(prompt)`)
- **Lock Screen**: `LockScreenRemoteDataSourceImpl` (`aiClient.generateJson(prompt)`)
- **Voice Game**:
  - `PronunciationSentenceGeneratorService` (`aiClient.generateJson(prompt)`)
  - `VoiceEvaluationService` (`aiClient.generateFromAudio(...)`)
- **Roleplay**: `GeminiRoleplayService` (`aiClient.generateJson(prompt)`)
- **Mind Reader**: `GeminiMindReaderService` (`aiClient.generateJson(prompt)`)
