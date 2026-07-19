package com.iti.linguaquest.features.voicechat.data.datasource

  import com.iti.linguaquest.features.voicechat.domain.model.VoiceChatEvent
  import kotlinx.coroutines.flow.Flow

interface VoiceChatRemoteDataSource {
    suspend fun connect()
    suspend fun close()
    suspend fun sendText(text: String)
    suspend fun sendAudioChunk(chunk: ByteArray)
    fun observeServerEvents(): Flow<VoiceChatEvent>
}
