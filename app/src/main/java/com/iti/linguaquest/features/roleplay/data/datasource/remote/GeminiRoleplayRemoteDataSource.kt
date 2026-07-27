package com.iti.linguaquest.features.roleplay.data.datasource.remote

import com.iti.linguaquest.features.roleplay.domain.model.BossEvaluationResult

interface GeminiRoleplayRemoteDataSource {
    suspend fun generateRoleplayTurn(systemPrompt: String, audioBytes: ByteArray?): String?
    suspend fun evaluateBossStage(transcript: List<String>, taskObjective: String, nativeLanguage: String): BossEvaluationResult?
}
