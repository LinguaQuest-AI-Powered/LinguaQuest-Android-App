package com.iti.linguaquest.core.ai

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.QuotaExceededException
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single shared entry point for all Gemini calls in the app, built on the
 * official Firebase AI SDK.
 */
@Singleton
class GeminiAiService @Inject constructor() {

    private val jsonTextModel by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(
                modelName = MODEL_NAME,
                generationConfig = generationConfig {
                    temperature = 0.7f
                    responseMimeType = "application/json"
                }
            )
    }

    private val jsonAudioModel by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(
                modelName = MODEL_NAME,
                generationConfig = generationConfig {
                    temperature = 0.0f
                    responseMimeType = "application/json"
                }
            )
    }

    /**
     * Sends a text-only prompt and returns the raw JSON text response, or null on failure.
     * Caller is responsible for its own fallback behavior (e.g. canned content) on null.
     */
    suspend fun generateJson(prompt: String): String? = withContext(Dispatchers.IO) {
        try {
            val response = jsonTextModel.generateContent(content { text(prompt) })
            cleanJson(response.text)
        } catch (e: QuotaExceededException) {
            null
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Sends a text prompt plus inline audio bytes and returns the raw JSON text response,
     * or null on failure.
     */
    suspend fun generateJsonFromAudio(
        prompt: String,
        audioBytes: ByteArray,
        mimeType: String = "audio/wav"
    ): String? = withContext(Dispatchers.IO) {
        try {
            val response = jsonAudioModel.generateContent(
                content {
                    text(prompt)
                    inlineData(audioBytes, mimeType)
                }
            )
            cleanJson(response.text)
        } catch (e: QuotaExceededException) {
            null
        } catch (e: Exception) {
            null
        }
    }

    private fun cleanJson(rawText: String?): String? {
        val cleaned = rawText
            ?.trim()
            ?.removePrefix("```json")
            ?.removePrefix("```")
            ?.removeSuffix("```")
            ?.trim()
        return cleaned?.takeIf { it.isNotEmpty() }
    }

    private companion object {
        const val TAG = "GeminiAiService"
        const val MODEL_NAME = "gemini-3.5-flash-lite"
    }
}