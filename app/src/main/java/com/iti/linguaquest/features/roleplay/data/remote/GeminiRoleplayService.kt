package com.iti.linguaquest.features.roleplay.data.remote

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

@Singleton
class GeminiRoleplayService @Inject constructor() : GeminiRoleplayRemoteDataSource {

    private val generativeModel by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(
                modelName = "gemini-3.6-flash",
                generationConfig = generationConfig {
                    temperature = 0.5f
                    responseMimeType = "application/json"
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
                        inlineData(audioBytes, "audio/mp4") // AAC in mp4 container
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
        val cleaned = rawText
            ?.trim()
            ?.removePrefix("```json")
            ?.removePrefix("```")
            ?.removeSuffix("```")
            ?.trim()
        return cleaned?.takeIf { it.isNotEmpty() }
    }

    override suspend fun evaluateBossStage(
        transcript: List<String>,
        taskObjective: String,
        nativeLanguage: String
    ): String? = withContext(Dispatchers.IO) {
        val transcriptText = transcript.joinToString("\n")
        val systemPrompt = """
            You are a language evaluator. 
            Evaluate the following transcript based on this objective: "$taskObjective"
            Output a JSON object containing EXACTLY these keys:
            - "task_completed" (boolean)
            - "fluency_score" (integer between 0 and 100)
            - "feedback_message" (string written strictly in $nativeLanguage)
            
            Transcript:
            $transcriptText
        """.trimIndent()
        
        try {
            val response = generativeModel.generateContent(
                content {
                    text(systemPrompt)
                }
            )
            cleanJson(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
