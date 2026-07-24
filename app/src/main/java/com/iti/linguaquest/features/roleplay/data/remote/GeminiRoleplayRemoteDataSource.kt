package com.iti.linguaquest.features.roleplay.data.remote

interface GeminiRoleplayRemoteDataSource {
    suspend fun generateRoleplayTurn(systemPrompt: String, audioBytes: ByteArray?): String?
    suspend fun evaluateBossStage(transcript: List<String>, taskObjective: String, nativeLanguage: String): String?
}
