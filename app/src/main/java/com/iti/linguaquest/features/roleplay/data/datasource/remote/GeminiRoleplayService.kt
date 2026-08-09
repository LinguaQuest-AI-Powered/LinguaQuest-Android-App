package com.iti.linguaquest.features.roleplay.data.datasource.remote

import com.google.gson.Gson
import com.iti.linguaquest.core.ai.network.GeminiRestClient
import com.iti.linguaquest.core.ai.network.model.GeminiContentDto
import com.iti.linguaquest.core.ai.network.model.GeminiGenerationConfigDto
import com.iti.linguaquest.core.ai.network.model.GeminiPartDto
import com.iti.linguaquest.core.ai.network.model.GeminiRequestDto
import com.iti.linguaquest.features.roleplay.domain.model.BossEvaluationResult
import com.iti.linguaquest.features.roleplay.domain.prompt.PromptFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiRoleplayService @Inject constructor(
    private val geminiRestClient: GeminiRestClient,
    private val gson: Gson
) : GeminiRoleplayRemoteDataSource {

    override suspend fun generateRoleplayTurn(
        systemPrompt: String,
        audioBytes: ByteArray?
    ): String? = withContext(Dispatchers.IO) {
        try {
            val responseText = executeGeminiRequest(systemPrompt)
            cleanJson(responseText)
        } catch (e: Exception) {
            Timber.e(e, "Error during generateRoleplayTurn")
            null
        }
    }

    override suspend fun evaluateBossStage(
        transcript: List<String>,
        taskObjective: String,
        nativeLanguage: String,
        targetLanguage: String
    ): BossEvaluationResult? = withContext(Dispatchers.IO) {
        val transcriptText = transcript.joinToString("\n")
        val systemPrompt = PromptFactory.createBossEvaluationPrompt(
            transcriptText = transcriptText,
            taskObjective = taskObjective,
            nativeLanguage = nativeLanguage,
            targetLanguage = targetLanguage
        )

        try {
            val rawText = executeGeminiRequest(systemPrompt)
            val jsonString = cleanJson(rawText)

            jsonString?.let {
                gson.fromJson(it, BossEvaluationResult::class.java)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error during evaluateBossStage")
            throw e
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
                temperature = 0.1f,
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

