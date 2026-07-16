package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.model.AuthUser
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        username: String,
        password: String,
        nativeLanguage: String,
        targetLanguage: String
    ): LinguaQuestResult<Unit, AuthError>  = authRepository.register(
            email = email, username, password, nativeLanguage, targetLanguage)

}
