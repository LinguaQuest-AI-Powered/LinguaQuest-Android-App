package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.network.EmptyResult
import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.features.auth.domain.repo.AuthRepository
import javax.inject.Inject

class VerifyEmailOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, otpCode: String): EmptyResult<LinguaQuestDataError> {
        return repository.verifyEmailOtp(email = email, otpCode = otpCode)
    }
}