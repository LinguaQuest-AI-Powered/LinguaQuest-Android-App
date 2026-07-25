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
    ): String? = withContext(Dispatchers.IO) {
        val transcriptText = transcript.joinToString("\n")
        android.util.Log.d("RoleplayDebug", "Evaluating transcript. Size: ${transcript.size}, Content:\n$transcriptText")
        val systemPrompt = """
            You are a roleplay evaluator. 
            The following transcript contains the dialogue of a roleplay session between the User and the AI Boss.
            
            Evaluate if the user achieved this objective: "$taskObjective"
            
            Evaluate the user's 'fluency_score' (0-100) based on their grammar, vocabulary, and conversational flow as shown in the transcript.
            Write a 'feedback_message' (in $nativeLanguage) summarizing how they handled the scenario.
            
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
