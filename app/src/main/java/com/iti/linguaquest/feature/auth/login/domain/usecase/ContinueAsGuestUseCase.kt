package com.iti.linguaquest.feature.auth.login.domain.usecase

import com.iti.linguaquest.feature.auth.login.domain.repository.LoginRepository
import javax.inject.Inject

class ContinueAsGuestUseCase @Inject constructor(
    private val repository: LoginRepository,
) {
    suspend operator fun invoke() = repository.continueAsGuest()
}
