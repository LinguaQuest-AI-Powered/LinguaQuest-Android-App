package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginWithEmailUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ) = repository.loginWithEmail(email, password)
}
