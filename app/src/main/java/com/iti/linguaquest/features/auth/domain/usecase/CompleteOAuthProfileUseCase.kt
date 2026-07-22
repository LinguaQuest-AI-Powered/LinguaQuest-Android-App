package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class CompleteOAuthProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        nativeLanguageId: Int,
        targetLanguageId: Int,
        username: String? = null
    ): LinguaQuestResult<Unit, AuthError> {
        return authRepository.completeOAuthProfile(
            nativeLanguageId = nativeLanguageId,
            targetLanguageId = targetLanguageId,
            username = username
        )
    }
}
