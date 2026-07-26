package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import javax.inject.Inject

class DisconnectRoleplayUseCase @Inject constructor(
    private val repository: RoleplayRepository
) {
    suspend operator fun invoke() {
        repository.disconnect()
    }
}
