package com.iti.linguaquest.features.roleplay.data.datasource.remote

import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import kotlinx.coroutines.flow.Flow

interface LiveRoleplayRemoteDataSource {
    suspend fun connect(systemPrompt: String)
    suspend fun sendAudioChunk(chunk: ByteArray)
    fun observeServerEvents(): Flow<RoleplayLiveEvent>
    suspend fun close()
}
