package com.iti.linguaquest.feature.auth.login.domain.usecase

import com.iti.linguaquest.feature.auth.login.domain.repository.LoginRepository
import javax.inject.Inject

class LoginWithEmailUseCase @Inject constructor(
    private val repository: LoginRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ) = repository.loginWithEmail(email, password)
}
