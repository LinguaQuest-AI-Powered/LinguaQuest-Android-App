package com.iti.linguaquest.features.roleplay.data.repository

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.roleplay.data.remote.GeminiRoleplayService
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayResult
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayTurnResponse
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import kotlinx.coroutines.flow.firstOrNull
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoleplayRepositoryImpl @Inject constructor(
    private val geminiService: GeminiRoleplayService,
    private val userPreferencesRepository: UserPreferencesRepository
) : RoleplayRepository {

    private val turnHistory = mutableListOf<String>()
    private var currentSetting: String = ""
    private var currentTaskDescription: String = ""
    private var targetLanguage: String = "English"

    override suspend fun initializeRoleplay(
        setting: String,
        taskDescription: String
    ): LinguaQuestResult<RoleplayTurnResponse, LinguaQuestDataError> {
        turnHistory.clear()
        currentSetting = setting
        currentTaskDescription = taskDescription
        targetLanguage = userPreferencesRepository.targetLanguageName.firstOrNull() ?: "English"

        val systemPrompt = buildSystemPrompt()
        // For initialization, we ask the AI to start the conversation without user audio
        val jsonString = geminiService.generateRoleplayTurn(systemPrompt, null)
            ?: return LinguaQuestResult.Failure(LinguaQuestDataError.Remote.UNKNOWN)

        return parseTurnResponse(jsonString)
    }

    override suspend fun submitUserAudio(
        audioBytes: ByteArray
    ): LinguaQuestResult<RoleplayTurnResponse, LinguaQuestDataError> {
        val systemPrompt = buildSystemPrompt()
        
        val jsonString = geminiService.generateRoleplayTurn(systemPrompt, audioBytes)
            ?: return LinguaQuestResult.Failure(LinguaQuestDataError.Remote.UNKNOWN)

        return parseTurnResponse(jsonString)
    }

    override suspend fun evaluateRoleplay(): LinguaQuestResult<RoleplayResult, LinguaQuestDataError> {
        return LinguaQuestResult.Success(
            RoleplayResult(
                passed = true, // Simplified for now
                coinsAwarded = 50,
                feedback = "Great effort! You maintained the conversation well.",
                totalTurns = turnHistory.size / 2
            )
        )
    }

    private fun buildSystemPrompt(): String = buildString {
        append("You are 'Lingo', a friendly mascot and AI language tutor playing a roleplay game with the user.\n")
        append("The language you are speaking is: $targetLanguage.\n")
        append("The setting of the roleplay is: $currentSetting.\n")
        append("The user's objective is: $currentTaskDescription.\n\n")
        append("This is what has happened so far in the conversation:\n")
        if (turnHistory.isEmpty()) {
            append("- The conversation is just starting.\n")
        } else {
            turnHistory.forEach { append("- $it\n") }
        }
        append("\nAnalyze the user's attached spoken audio (if any). Then respond strictly in JSON format matching this schema:\n")
        append("{\n")
        append("  \"aiText\": \"Your conversational response in $targetLanguage to keep the roleplay going\",\n")
        append("  \"aiTranslation\": \"The English translation of your response\",\n")
        append("  \"isObjectiveComplete\": boolean indicating if the user successfully completed their objective ($currentTaskDescription)\n")
        append("}\n")
        append("Do not include markdown blocks, just raw JSON.")
    }

    private fun parseTurnResponse(jsonString: String): LinguaQuestResult<RoleplayTurnResponse, LinguaQuestDataError> {
        return try {
            val jsonObject = JSONObject(jsonString)
            val aiText = jsonObject.getString("aiText")
            val aiTranslation = jsonObject.getString("aiTranslation")
            val isObjectiveComplete = jsonObject.getBoolean("isObjectiveComplete")

            turnHistory.add("Lingo: $aiText")

            LinguaQuestResult.Success(
                RoleplayTurnResponse(
                    aiText = aiText,
                    aiTranslation = aiTranslation,
                    audioBytes = ByteArray(0), // Handled by TTS in UI
                    isObjectiveComplete = isObjectiveComplete,
                    turnNumber = turnHistory.size / 2
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LinguaQuestResult.Failure(LinguaQuestDataError.Remote.SERIALIZATION)
        }
    }
}
