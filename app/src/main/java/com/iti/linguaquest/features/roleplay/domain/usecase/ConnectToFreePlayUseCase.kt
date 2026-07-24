package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import javax.inject.Inject

class ConnectToFreePlayUseCase @Inject constructor(
    private val repository: RoleplayRepository
) {
    suspend operator fun invoke(targetLanguage: String) {
        repository.connectToFreePlay(targetLanguage)
    }
}
