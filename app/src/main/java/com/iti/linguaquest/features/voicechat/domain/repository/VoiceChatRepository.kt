package com.iti.linguaquest.features.voicechat.domain.repository

import com.iti.linguaquest.features.voicechat.domain.model.VoiceChatEvent
import kotlinx.coroutines.flow.Flow

interface VoiceChatRepository {
    suspend fun connect()
    fun disconnect()
    suspend fun sendText(text: String)
    fun startListening()
    fun stopListening()
    val events: Flow<VoiceChatEvent>
}
