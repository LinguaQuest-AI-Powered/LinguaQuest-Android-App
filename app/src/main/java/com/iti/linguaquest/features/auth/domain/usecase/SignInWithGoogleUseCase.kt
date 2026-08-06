package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.features.notification.domain.usecase.RegisterDeviceTokenUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val registerDeviceTokenUseCase: RegisterDeviceTokenUseCase
) {
    suspend operator fun invoke(idToken: String): LinguaQuestResult<Boolean, AuthError> {
        val result = authRepository.signInWithGoogle(idToken)
        if (result is LinguaQuestResult.Success && result.data) {
            try {
                withTimeoutOrNull(3_000L) {
                    registerDeviceTokenUseCase()
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to register device token after Google sign in")
            }
        }
        return result
    }
}
