package com.iti.linguaquest.features.roleplay.domain.repository

import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayAssessmentResult
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import kotlinx.coroutines.flow.Flow

interface RoleplayRepository {
    val events: Flow<RoleplayLiveEvent>

    suspend fun connect(systemPrompt: String)
    suspend fun connectToFreePlay(targetLanguage: String)
    suspend fun connectToBossStage(scenario: BossScenario)
    suspend fun evaluateBossStage(transcript: List<String>, scenario: BossScenario): Result<RoleplayAssessmentResult>
    suspend fun disconnect()
    
    fun startMicrophone()
    fun stopMicrophone()
}
