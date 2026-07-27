package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import javax.inject.Inject

class StopMicrophoneUseCase @Inject constructor(
    private val repository: RoleplayRepository
) {
    operator fun invoke() {
        repository.stopMicrophone()
    }
}
