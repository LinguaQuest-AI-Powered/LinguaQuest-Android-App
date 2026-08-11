package com.iti.linguaquest.core.ai.network

import com.iti.linguaquest.BuildConfig
import com.iti.linguaquest.core.ai.network.model.GeminiRequestDto
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiRestClient @Inject constructor(
    private val apiService: GeminiApiService
) {
    private val candidateModels = listOf(
        "gemini-1.5-flash",
        "gemini-2.0-flash",
        "gemini-3.5-flash-lite",
        "gemini-flash-latest",
        "gemini-3.1-flash-lite",
        "gemini-3.5-flash"
    )

    suspend fun executeGeminiRequest(requestPayload: GeminiRequestDto): String? {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) {
            Timber.e("GEMINI_API_KEY is empty in BuildConfig")
            return null
        }

        var lastException: Throwable? = null
        var lastHttpCode: Int? = null

        for (model in candidateModels) {
            try {
                val response = apiService.generateContent(
                    model = model,
                    apiKey = apiKey,
                    request = requestPayload
                )

                if (response.isSuccessful) {
                    val resultText = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    if (!resultText.isNullOrBlank()) {
                        return resultText
                    }
                } else {
                    lastHttpCode = response.code()
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
}
