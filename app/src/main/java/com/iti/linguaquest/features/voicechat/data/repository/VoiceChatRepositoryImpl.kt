package com.iti.linguaquest.features.voicechat.data.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.voicechat.data.datasource.VoiceChatRemoteDataSource
import com.iti.linguaquest.features.voicechat.domain.model.VoiceChatResponse
import com.iti.linguaquest.features.voicechat.domain.repository.VoiceChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VoiceChatRepositoryImpl @Inject constructor(
    private val remoteDataSource: VoiceChatRemoteDataSource
) : VoiceChatRepository {

    override suspend fun connect(systemInstructions: String): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return remoteDataSource.connect(systemInstructions)
    }

    override suspend fun sendAudioChunk(audioBytes: ByteArray): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return remoteDataSource.sendAudioChunk(audioBytes)
    }

    override fun receiveChatStream(): Flow<VoiceChatResponse> {
        return remoteDataSource.receiveChatStream()
    }

    override suspend fun disconnect(): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return remoteDataSource.disconnect()
    }
}
