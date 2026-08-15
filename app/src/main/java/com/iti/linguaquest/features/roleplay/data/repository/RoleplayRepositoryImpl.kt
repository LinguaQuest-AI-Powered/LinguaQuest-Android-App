package com.iti.linguaquest.features.roleplay.data.repository

import com.iti.linguaquest.core.ai.roleplay.LiveRoleplayRemoteDataSource
import com.iti.linguaquest.core.cache.data.datasource.UserPreferencesLocalDataSource
import com.iti.linguaquest.features.roleplay.data.datasource.remote.GeminiRoleplayRemoteDataSource
import com.iti.linguaquest.features.roleplay.domain.model.BossEvaluationResult
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import com.iti.linguaquest.features.roleplay.domain.prompt.PromptFactory
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoleplayRepositoryImpl @Inject constructor(
    private val liveService: LiveRoleplayRemoteDataSource,
    private val geminiService: GeminiRoleplayRemoteDataSource,
    private val userPreferences: UserPreferencesLocalDataSource
) : RoleplayRepository {

    override val events: Flow<RoleplayLiveEvent> = liveService.observeServerEvents()

    override suspend fun connect(systemPrompt: String, voiceName: String) {
        val targetLanguage = userPreferences.targetLanguageName.firstOrNull() ?: "English"
        liveService.connect(systemPrompt, voiceName, targetLanguage)
    }

    override suspend fun connectToBossStage(scenario: BossScenario) {
        val targetLanguage = userPreferences.targetLanguageName.firstOrNull() ?: "English"
        val systemPrompt = PromptFactory.createLiveSessionPrompt(
            bossName = scenario.bossName,
            roleDescription = scenario.roleDescription,
            objective = scenario.objective,
            targetLanguage = targetLanguage
        )
        liveService.connect(systemPrompt, scenario.voiceName, targetLanguage)
    }

    override suspend fun evaluateBossStage(transcript: List<String>, scenario: BossScenario): Result<BossEvaluationResult> {
        return try {
            val nativeLanguage = userPreferences.nativeLanguageName.firstOrNull() ?: "English"
            val targetLanguage = userPreferences.targetLanguageName.firstOrNull() ?: "English"
            val evaluationResult = geminiService.evaluateBossStage(
                transcript = transcript,
                taskObjective = scenario.objective,
                nativeLanguage = nativeLanguage,
                targetLanguage = targetLanguage
            )
            
            if (evaluationResult != null) {
                Result.success(evaluationResult)
            } else {
                Timber.w("[Repo] evaluateBossStage() — null result from Gemini")
                Result.failure(IllegalStateException("Failed to generate assessment JSON from Gemini"))
            }
        } catch (e: Exception) {
            Timber.e(e, "[Repo] evaluateBossStage() failed")
            Result.failure(e)
        }
    }
    
    override fun startMicrophone() {
        liveService.startMicrophone()
    }
    
    override fun stopMicrophone() {
        liveService.stopMicrophone()
    }

    override suspend fun disconnect() {
        liveService.close()
    }
}
