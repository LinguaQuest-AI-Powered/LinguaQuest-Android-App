package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.features.notification.domain.usecase.UnregisterDeviceTokenUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val unregisterDeviceTokenUseCase: UnregisterDeviceTokenUseCase
) {
    suspend operator fun invoke(): LinguaQuestResult<Unit, AuthError> {
        try {
            withTimeoutOrNull(3_000L) {
                unregisterDeviceTokenUseCase()
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to unregister device token during logout")
        }
        return authRepository.logout()
    }
}
