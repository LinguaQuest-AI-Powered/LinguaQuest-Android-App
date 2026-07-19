package com.iti.linguaquest.features.voicechat.domain.usecase

 import com.iti.linguaquest.features.voicechat.domain.repository.VoiceChatRepository
 import com.iti.linguaquest.features.voicechat.domain.model.VoiceChatEvent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveVoiceChatEventsUseCase @Inject constructor(
    private val repository: VoiceChatRepository
) {
    operator fun invoke(): Flow<VoiceChatEvent> = repository.events
}
