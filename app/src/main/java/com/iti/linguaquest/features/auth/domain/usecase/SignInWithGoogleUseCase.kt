package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthUserModel
import com.iti.linguaquest.features.auth.domain.repo.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): LinguaQuestResult<AuthUserModel, LinguaQuestDataError> {
        return repository.signInWithGoogle(idToken = idToken)
    }
}