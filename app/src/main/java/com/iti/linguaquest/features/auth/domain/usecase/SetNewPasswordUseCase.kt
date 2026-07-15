package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.network.EmptyResult
import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SetNewPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(newPassword: String, resetToken: String): LinguaQuestResult<Unit, AuthError> {
        return authRepository.setNewPassword(newPassword, resetToken)
    }
}