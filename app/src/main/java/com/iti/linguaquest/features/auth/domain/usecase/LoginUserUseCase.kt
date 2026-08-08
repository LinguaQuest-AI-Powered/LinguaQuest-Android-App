package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.features.notification.domain.usecase.RegisterDeviceTokenUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import com.iti.linguaquest.core.result.map
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val registerDeviceTokenUseCase: RegisterDeviceTokenUseCase,
    private val syncUserNativeLanguageUseCase: SyncUserNativeLanguageUseCase
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): LinguaQuestResult<Unit, AuthError> {
        val result = authRepository.login(email, password)
        if (result is LinguaQuestResult.Success) {
            try {
                syncUserNativeLanguageUseCase(result.data.nativeLanguage)
            } catch (e: Exception) {
                Timber.e(e, "Failed to sync native language")
            }

            try {
                withTimeoutOrNull(3_000L) {
                    registerDeviceTokenUseCase()
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to register device token after login")
            }
        }
        return result.map { Unit }
    }
}


