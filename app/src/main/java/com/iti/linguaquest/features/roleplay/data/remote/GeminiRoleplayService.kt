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
        android.util.Log.d("RoleplayDebug", "Evaluating transcript. Size: ${transcript.size}, Content:\n$transcriptText")
        val systemPrompt = """
            You are a roleplay evaluator. 
            The following transcript contains the AI Boss's internal chain-of-thought logs during a roleplay session with the user.
            You do NOT have the user's direct speech. You must infer the user's actions and success entirely from reading how the AI reacted in these logs.
            
            Evaluate if the user achieved this objective: "$taskObjective"
            
            Because you cannot see the user's exact grammar, estimate a 'fluency_score' (0-100) based on how smoothly the AI's thoughts indicate the conversation went. 
            Write a 'feedback_message' (in $nativeLanguage) summarizing how they handled the scenario based on the AI's reactions.
            
            Return ONLY a valid JSON object matching this schema exactly:
            {
              "task_completed": boolean,
              "fluency_score": integer,
              "feedback_message": "string"
            }
            
            Transcript Logs:
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
