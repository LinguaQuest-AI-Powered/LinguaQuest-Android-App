package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.repo.AuthRepository
import javax.inject.Inject

class VerifyPasswordResetOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, otpCode: String): LinguaQuestResult<String, LinguaQuestDataError> {
        return repository.verifyPasswordResetOtp(email = email, otpCode = otpCode)
    }
}