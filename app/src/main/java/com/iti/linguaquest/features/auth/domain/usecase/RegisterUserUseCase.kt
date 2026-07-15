package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthUserModel
import com.iti.linguaquest.features.auth.domain.repo.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        username: String,
        password: String,
        nativeLanguage: String,
        targetLanguage: String
    ): LinguaQuestResult<AuthUserModel, LinguaQuestDataError> {
        return repository.register(
            email = email,
            username = username,
            password = password,
            nativeLanguage = nativeLanguage,
            targetLanguage = targetLanguage
        )
    }
}