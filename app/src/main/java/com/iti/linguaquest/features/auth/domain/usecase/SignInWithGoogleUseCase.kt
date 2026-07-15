package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.model.AuthUser
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): LinguaQuestResult<AuthUser, AuthError> {
        return authRepository.signInWithGoogle(idToken)
    }
}