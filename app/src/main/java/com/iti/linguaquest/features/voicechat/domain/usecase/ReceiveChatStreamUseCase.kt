package com.iti.linguaquest.features.voicechat.domain.usecase

import com.iti.linguaquest.features.voicechat.domain.model.VoiceChatResponse
import com.iti.linguaquest.features.voicechat.domain.repository.VoiceChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReceiveChatStreamUseCase @Inject constructor(
    private val repository: VoiceChatRepository
) {
    operator fun invoke(): Flow<VoiceChatResponse> {
        return repository.receiveChatStream()
    }
}
