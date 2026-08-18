package com.iti.linguaquest.features.mindreader.data.datasource.remote

import com.google.gson.Gson
import com.iti.linguaquest.core.ai.client.AiClient
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
    private val aiClient: AiClient,
    private val gson: Gson
) : MindReaderRemoteDataSource {

    override suspend fun getNextTurn(prompt: String): MindReaderNextTurnDto? = withContext(Dispatchers.IO) {
        try {
            val jsonString = aiClient.generateJson(prompt, temperature = 0.3f)
            val cleaned = cleanJson(jsonString) ?: return@withContext null
            gson.fromJson(cleaned, MindReaderNextTurnDto::class.java)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get next turn from Gemini")
            null
        }
    }

    override suspend fun generateQuizChoices(prompt: String): MindReaderQuizDto? = withContext(Dispatchers.IO) {
        try {
            val jsonString = aiClient.generateJson(prompt, temperature = 0.3f)
            val cleaned = cleanJson(jsonString) ?: return@withContext null
            gson.fromJson(cleaned, MindReaderQuizDto::class.java)
        } catch (e: Exception) {
            Timber.e(e, "Failed to generate quiz choices from Gemini")
            null
        }
    }

    override suspend fun verifyUserWord(prompt: String): MindReaderHonestyDto? = withContext(Dispatchers.IO) {
        try {
            val jsonString = aiClient.generateJson(prompt, temperature = 0.1f)
            val cleaned = cleanJson(jsonString) ?: return@withContext null
            gson.fromJson(cleaned, MindReaderHonestyDto::class.java)
        } catch (e: Exception) {
            Timber.e(e, "Failed to verify user word from Gemini")
            null
        }
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
