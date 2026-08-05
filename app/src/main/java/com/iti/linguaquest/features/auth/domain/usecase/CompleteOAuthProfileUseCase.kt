package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.notification.domain.usecase.RegisterDeviceTokenUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import javax.inject.Inject

class CompleteOAuthProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val registerDeviceTokenUseCase: RegisterDeviceTokenUseCase
) {
    suspend operator fun invoke(
        nativeLanguageId: Int,
        targetLanguageId: Int,
        username: String? = null
    ): LinguaQuestResult<Unit, AuthError> {
        val result = authRepository.completeOAuthProfile(
            nativeLanguageId = nativeLanguageId,
            targetLanguageId = targetLanguageId,
            username = username
        )
        if (result is LinguaQuestResult.Success) {
            try {
                withTimeoutOrNull(3_000L) {
                    registerDeviceTokenUseCase()
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to register device token after completing OAuth profile")
            }
        }
        return result
    }
}


