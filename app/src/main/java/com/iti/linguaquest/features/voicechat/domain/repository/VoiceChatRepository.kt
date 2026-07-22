package com.iti.linguaquest.features.voicechat.domain.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.voicechat.domain.model.VoiceChatResponse
import kotlinx.coroutines.flow.Flow

interface VoiceChatRepository {
    suspend fun connect(systemInstructions: String): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun sendAudioChunk(audioBytes: ByteArray): LinguaQuestResult<Unit, LinguaQuestDataError>
    fun receiveChatStream(): Flow<VoiceChatResponse>
    suspend fun disconnect(): LinguaQuestResult<Unit, LinguaQuestDataError>
}
