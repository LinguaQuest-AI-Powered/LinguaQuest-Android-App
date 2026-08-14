package com.iti.linguaquest.features.mindreader.data.datasource.remote

import com.google.gson.Gson
import com.iti.linguaquest.core.ai.network.GeminiRestClient
import com.iti.linguaquest.core.ai.network.model.GeminiContentDto
import com.iti.linguaquest.core.ai.network.model.GeminiGenerationConfigDto
import com.iti.linguaquest.core.ai.network.model.GeminiPartDto
import com.iti.linguaquest.core.ai.network.model.GeminiRequestDto
import com.iti.linguaquest.features.mindreader.data.dto.MindReaderHonestyDto
import com.iti.linguaquest.features.mindreader.data.dto.MindReaderNextTurnDto
import com.iti.linguaquest.features.mindreader.data.dto.MindReaderQuizDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiMindReaderService @Inject constructor(
    private val geminiRestClient: GeminiRestClient,
    private val gson: Gson
) : MindReaderRemoteDataSource {

    override suspend fun getNextTurn(prompt: String): MindReaderNextTurnDto? = withContext(Dispatchers.IO) {
        try {
            val rawResponse = executeGeminiRequest(prompt)
            val jsonString = cleanJson(rawResponse) ?: return@withContext null
            gson.fromJson(jsonString, MindReaderNextTurnDto::class.java)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get next turn from Gemini")
            null
        }
    }

    override suspend fun generateQuizChoices(prompt: String): MindReaderQuizDto? = withContext(Dispatchers.IO) {
        try {
            val rawResponse = executeGeminiRequest(prompt)
            val jsonString = cleanJson(rawResponse) ?: return@withContext null
            gson.fromJson(jsonString, MindReaderQuizDto::class.java)
        } catch (e: Exception) {
            Timber.e(e, "Failed to generate quiz choices from Gemini")
            null
        }
    }

    override suspend fun verifyUserWord(prompt: String): MindReaderHonestyDto? = withContext(Dispatchers.IO) {
        try {
            val rawResponse = executeGeminiRequest(prompt)
            val jsonString = cleanJson(rawResponse) ?: return@withContext null
            gson.fromJson(jsonString, MindReaderHonestyDto::class.java)
        } catch (e: Exception) {
            Timber.e(e, "Failed to verify user word from Gemini")
            null
        }
    }

    private suspend fun executeGeminiRequest(promptText: String): String? {
        val requestPayload = GeminiRequestDto(
            contents = listOf(
                GeminiContentDto(
                    parts = listOf(GeminiPartDto(text = promptText))
                )
            ),
            generationConfig = GeminiGenerationConfigDto(
                temperature = 0.5f,
                responseMimeType = "application/json"
            )
        )
        return geminiRestClient.executeGeminiRequest(requestPayload)
    }

    private fun cleanJson(rawText: String?): String? {
        if (rawText.isNullOrBlank()) return null
        val stripped = rawText.replace("```json", "").replace("```", "").trim()
        val startIndex = stripped.indexOf('{')
        val endIndex = stripped.lastIndexOf('}')
        if (startIndex != -1 && endIndex != -1 && startIndex <= endIndex) {
            return stripped.substring(startIndex, endIndex + 1)
        }
        return null
    }
}
