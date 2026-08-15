package com.iti.linguaquest.core.ai.roleplay

import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import kotlinx.coroutines.flow.Flow

interface LiveRoleplayRemoteDataSource {
    suspend fun connect(systemPrompt: String, voiceName: String, targetLanguage: String = "English")
    suspend fun sendAudioChunk(chunk: ByteArray)
    fun startMicrophone()
    fun stopMicrophone()
    fun observeServerEvents(): Flow<RoleplayLiveEvent>
    suspend fun close()
}
