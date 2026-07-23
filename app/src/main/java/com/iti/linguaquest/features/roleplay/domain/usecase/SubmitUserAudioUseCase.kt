package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayTurnResponse
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import javax.inject.Inject

class SubmitUserAudioUseCase @Inject constructor(
    private val repository: RoleplayRepository
) {
    suspend operator fun invoke(
        audioBytes: ByteArray
    ): LinguaQuestResult<RoleplayTurnResponse, LinguaQuestDataError> {
        return repository.submitUserAudio(audioBytes)
    }
}
