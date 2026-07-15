package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.model.AuthLoginResult
import com.iti.linguaquest.features.auth.domain.model.AuthUser
import kotlinx.coroutines.delay
import javax.inject.Inject

class SignUpWithEmailUseCase @Inject constructor() {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String,
    ): LinguaQuestResult<AuthLoginResult, AuthError> {
        return LinguaQuestResult.Success(
            AuthLoginResult(
                accessToken = "mock_access_token",
                refreshToken = "mock_refresh_token",
                tokenType = "Bearer",
                expiresIn = 3600,
                user = AuthUser(
                    id = 1L,
                    username = username,
                    name = username,
                    photo = null,
                    nativeLanguage = null,
                    isVerified = false
                )
            )
        )
    }
}
