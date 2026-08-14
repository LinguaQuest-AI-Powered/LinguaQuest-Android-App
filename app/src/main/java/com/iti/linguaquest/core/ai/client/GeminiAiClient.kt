package com.iti.linguaquest.core.ai.client

import android.util.Base64
import com.iti.linguaquest.BuildConfig
import com.iti.linguaquest.core.ai.network.GeminiApiService
import com.iti.linguaquest.core.ai.network.model.GeminiContentDto
import com.iti.linguaquest.core.ai.network.model.GeminiGenerationConfigDto
import com.iti.linguaquest.core.ai.network.model.GeminiInlineDataDto
import com.iti.linguaquest.core.ai.network.model.GeminiPartDto
import com.iti.linguaquest.core.ai.network.model.GeminiRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiAiClient @Inject constructor(
    private val apiService: GeminiApiService
) : AiClient {

    private val candidateModels = listOf(
        "gemini-3.5-flash-lite",
        "gemini-3.1-flash-lite",
        "gemini-3.5-flash",
        "gemini-3.7-flash"
    )

    private val invalidModels = Collections.synchronizedSet(mutableSetOf<String>())

    @Volatile
    private var activeModel: String = candidateModels.first()

    override suspend fun generateText(
        prompt: String,
        temperature: Float
    ): String? = withContext(Dispatchers.IO) {
        try {
            val payload = GeminiRequestDto(
                contents = listOf(
                    GeminiContentDto(
                        parts = listOf(GeminiPartDto(text = prompt))
                    )
                ),
                generationConfig = GeminiGenerationConfigDto(
                    temperature = temperature
                )
            )
            executeRequest(payload)?.trim()
        } catch (e: Exception) {
            Timber.e(e, "GeminiAiClient generateText failed")
            null
        }
    }

    override suspend fun generateJson(
        prompt: String,
        temperature: Float
    ): String? = withContext(Dispatchers.IO) {
        try {
            val payload = GeminiRequestDto(
                contents = listOf(
                    GeminiContentDto(
                        parts = listOf(GeminiPartDto(text = prompt))
                    )
                ),
                generationConfig = GeminiGenerationConfigDto(
                    temperature = temperature,
                    responseMimeType = "application/json"
                )
            )
            val raw = executeRequest(payload)
            cleanJson(raw)
        } catch (e: Exception) {
            Timber.e(e, "GeminiAiClient generateJson failed")
            null
        }
    }

    override suspend fun generateFromAudio(
        prompt: String,
        audioBytes: ByteArray,
        mimeType: String,
        temperature: Float
    ): String? = withContext(Dispatchers.IO) {
        try {
            val base64Audio = Base64.encodeToString(audioBytes, Base64.NO_WRAP)
            val payload = GeminiRequestDto(
                contents = listOf(
                    GeminiContentDto(
                        parts = listOf(
                            GeminiPartDto(
                                inlineData = GeminiInlineDataDto(
                                    mimeType = mimeType,
                                    data = base64Audio
                                )
                            ),
                            GeminiPartDto(text = prompt)
                        )
                    )
                ),
                generationConfig = GeminiGenerationConfigDto(
                    temperature = temperature,
                    responseMimeType = "application/json"
                )
            )
            val raw = executeRequest(payload)
            cleanJson(raw)
        } catch (e: Exception) {
            Timber.e(e, "GeminiAiClient generateFromAudio failed")
            null
        }
    }

    private suspend fun executeRequest(requestPayload: GeminiRequestDto): String? {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) {
            Timber.e("GEMINI_API_KEY is empty in BuildConfig")
            return null
        }

        val modelsToTry = buildList {
            if (!invalidModels.contains(activeModel)) {
                add(activeModel)
            }
            candidateModels.forEach { model ->
                if (model != activeModel && !invalidModels.contains(model)) {
                    add(model)
                }
            }
        }

        var lastException: Throwable? = null
        var lastHttpCode: Int? = null

        for (model in modelsToTry) {
            try {
                val response = apiService.generateContent(
                    model = model,
                    apiKey = apiKey,
                    request = requestPayload
                )

                if (response.isSuccessful) {
                    val resultText = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    if (!resultText.isNullOrBlank()) {
                        activeModel = model
                        return resultText
                    }
                } else {
                    lastHttpCode = response.code()
                    if (response.code() == 404 || response.code() == 400) {
                        invalidModels.add(model)
                    }
                    Timber.w("Model $model returned HTTP ${response.code()}. Trying fallback...")
                }
            } catch (e: Exception) {
                lastException = e
                Timber.w(e, "Request to $model failed. Trying fallback...")
            }
        }

        Timber.e("All candidate Gemini models failed to generate response")
        if (lastHttpCode == 429 || lastException?.message?.contains("429") == true || lastException?.message?.contains("quota", ignoreCase = true) == true) {
            throw IllegalStateException("HTTP 429: Quota exceeded for Gemini AI service", lastException)
        }
        if (lastException != null) {
            throw lastException
        }
        return null
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
}
