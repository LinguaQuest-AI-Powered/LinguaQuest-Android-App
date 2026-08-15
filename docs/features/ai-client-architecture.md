# Centralized AI Client Architecture

## 1. Overview
LinguaQuest utilizes a centralized, provider-agnostic **`AiClient`** interface across all generative AI features (Review, Lock Screen vocabulary, Voice Game pronunciation & sentence generation, Roleplay boss stage evaluations, and Mind Reader).

This architecture decouples the app's business logic from specific AI vendors (ITI Gateway / DeepSeek / Llama, Gemini, Firebase), allowing provider swapping with a single line of code in Hilt DI.

---

## 2. Architecture & Layer Structure

```
core/ai/
├── client/
│   ├── AiClient.kt             <-- Provider-agnostic interface
│   ├── ItiGatewayAiClient.kt   <-- Implementation 1: ITI Multi-Model Gateway (DeepSeek, Llama, Voxtral, etc.)
│   ├── GatewayModel.kt         <-- Catalog of all 8 supported gateway models
│   ├── GeminiAiClient.kt       <-- Implementation 2: Direct Gemini REST + Adaptive Model Caching
│   └── FirebaseAiClient.kt     <-- Implementation 3: Firebase GoogleAI SDK (Firebase.ai)
├── di/
│   └── AiModule.kt             <-- Hilt binding for @Singleton AiClient
└── network/
    ├── ItiGatewayApiService.kt <-- Retrofit REST API interface for ITI Student AI Gateway
    ├── GeminiApiService.kt     <-- Retrofit REST API interface for Gemini
    └── model/
        ├── GatewayApiDto.kt    <-- OpenAI/Gateway Request/Response DTOs
        └── GeminiApiDto.kt     <-- Gemini Request/Response DTOs
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

### A. `ItiGatewayAiClient` (Primary Active Provider)
- Connects to the ITI Student AI Gateway (`BuildConfig.AI_BASE_URL`, configured via `local.properties`).
- Uses `BuildConfig.AI_KEY` for bearer authentication.
- **Task-Aware Multi-Model Routing**:
  - **JSON & Complex Reasoning** (`generateJson`): Primary `deepseek.v3.2`, fallback to `us.meta.llama3-3-70b-instruct-v1:0` and `openai.gpt-oss-120b-1:0`.
  - **General Text** (`generateText`): Primary `deepseek.v3.2` / `us.meta.llama3-3-70b-instruct-v1:0`, fallback to `openai.gpt-oss-20b-1:0`.
  - **Speech & Audio** (`generateFromAudio`): Dispatches to `mistral.voxtral-small-24b-2507` (Voxtral speech model).
- **Sticky Active Model Caching & Blacklisting**: Automatically caches working models and skips invalid models (HTTP 400/404).

#### Supported Models Catalog (`GatewayModel`)
1. `DEEPSEEK_V3_2`: `deepseek.v3.2`
2. `LLAMA_3_3_70B`: `us.meta.llama3-3-70b-instruct-v1:0`
3. `VOXTRAL_24B`: `mistral.voxtral-small-24b-2507`
4. `GPT_OSS_20B`: `openai.gpt-oss-20b-1:0`
5. `GPT_OSS_120B`: `openai.gpt-oss-120b-1:0`
6. `QWEN3_VL`: `qwen.qwen3-vl-235b-a22b`
7. `GPT_OSS_SAFEGUARD_120B`: `openai.gpt-oss-safeguard-120b`
8. `GPT_OSS_SAFEGUARD_20B`: `openai.gpt-oss-safeguard-20b`

### B. `GeminiAiClient` (Direct Google REST Provider)
- Connects directly to Google's Generative Language REST API via Retrofit (`GeminiApiService`).
- Sticky model caching with `gemini-3.5-flash-lite`, `gemini-3.1-flash-lite`, `gemini-3.5-flash`, `gemini-3.7-flash`.

### C. `FirebaseAiClient` (Firebase GoogleAI SDK Provider)
- Uses `com.google.firebase:firebase-vertexai` / `Firebase.ai` SDK.

---

## 5. Dependency Injection (`AiModule`)

To toggle between AI providers, change the `@Binds` implementation in `AiModule.kt`:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class AiModule {

    @Binds
    @Singleton
    abstract fun bindAiClient(impl: ItiGatewayAiClient): AiClient
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
