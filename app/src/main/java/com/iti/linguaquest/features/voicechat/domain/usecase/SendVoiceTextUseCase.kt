package com.iti.linguaquest.features.voicechat.domain.usecase

import com.iti.linguaquest.features.voicechat.domain.repository.VoiceChatRepository
import javax.inject.Inject

class SendVoiceTextUseCase @Inject constructor(
    private val repository: VoiceChatRepository
) {
    suspend operator fun invoke(text: String) = repository.sendText(text)
}
