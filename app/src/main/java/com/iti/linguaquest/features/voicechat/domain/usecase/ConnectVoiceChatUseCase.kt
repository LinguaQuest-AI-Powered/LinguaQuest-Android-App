package com.iti.linguaquest.features.voicechat.domain.usecase

import com.iti.linguaquest.features.voicechat.domain.repository.VoiceChatRepository
import javax.inject.Inject

class ConnectVoiceChatUseCase @Inject constructor(
    private val repository: VoiceChatRepository
) {
    suspend operator fun invoke() = repository.connect()
}
