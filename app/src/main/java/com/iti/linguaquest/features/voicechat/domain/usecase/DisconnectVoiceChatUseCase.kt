package com.iti.linguaquest.features.voicechat.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.voicechat.domain.repository.VoiceChatRepository
import javax.inject.Inject

class DisconnectVoiceChatUseCase @Inject constructor(
    private val repository: VoiceChatRepository
) {
    suspend operator fun invoke(): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return repository.disconnect()
    }
}
