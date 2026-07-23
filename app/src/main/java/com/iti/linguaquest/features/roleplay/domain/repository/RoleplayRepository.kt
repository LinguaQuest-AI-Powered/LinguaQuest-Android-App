package com.iti.linguaquest.features.roleplay.domain.repository

import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import kotlinx.coroutines.flow.Flow

interface RoleplayRepository {
    val events: Flow<RoleplayLiveEvent>

    suspend fun connect(systemPrompt: String)
    suspend fun disconnect()
    
    fun startMicrophone()
    fun stopMicrophone()
}
