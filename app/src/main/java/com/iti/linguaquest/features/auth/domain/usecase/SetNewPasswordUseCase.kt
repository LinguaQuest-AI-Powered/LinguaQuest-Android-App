package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.network.EmptyResult
import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.features.auth.domain.repo.AuthRepository
import javax.inject.Inject

class SetNewPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(newPassword: String, resetToken: String): EmptyResult<LinguaQuestDataError> {
        return repository.setNewPassword(newPassword = newPassword, resetToken = resetToken)
    }
}