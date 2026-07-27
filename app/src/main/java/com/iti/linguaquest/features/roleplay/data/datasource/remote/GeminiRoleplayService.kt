package com.iti.linguaquest.features.roleplay.data.datasource.remote

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.QuotaExceededException
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import com.google.gson.Gson
import com.iti.linguaquest.features.roleplay.domain.model.BossEvaluationResult
import com.iti.linguaquest.features.roleplay.domain.prompt.PromptFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiRoleplayService @Inject constructor() : GeminiRoleplayRemoteDataSource {

    private val generativeModel by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(
                modelName = "gemini-3.5-flash-lite",
                generationConfig = generationConfig {
                    temperature = 0.5f
                }
            )
    }

    override suspend fun generateRoleplayTurn(
        systemPrompt: String,
        audioBytes: ByteArray?
    ): String? = withContext(Dispatchers.IO) {
        try {
            val response = generativeModel.generateContent(
                content {
                    text(systemPrompt)
                    if (audioBytes != null && audioBytes.isNotEmpty()) {
                        inlineData(audioBytes, "audio/mp4")
                    }
                }
            )
            cleanJson(response.text)
        } catch (e: QuotaExceededException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun cleanJson(rawText: String?): String? {
        if (rawText.isNullOrBlank()) return null
        val startIndex = rawText.indexOf('{')
        val endIndex = rawText.lastIndexOf('}')
        if (startIndex != -1 && endIndex != -1 && startIndex <= endIndex) {
            return rawText.substring(startIndex, endIndex + 1)
        }
        return null
    }

    override suspend fun evaluateBossStage(
        transcript: List<String>,
        taskObjective: String,
        nativeLanguage: String
    ): BossEvaluationResult? = withContext(Dispatchers.IO) {
        val transcriptText = transcript.joinToString("\n")
        val systemPrompt = PromptFactory.createBossEvaluationPrompt(
            transcriptText = transcriptText,
            taskObjective = taskObjective,
            nativeLanguage = nativeLanguage
        )
        
        try {
            val response = generativeModel.generateContent(
                content {
                    text(systemPrompt)
                }
            )
            val jsonString = cleanJson(response.text)
            
            
            jsonString?.let {
                Gson().fromJson(it, BossEvaluationResult::class.java) 
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
