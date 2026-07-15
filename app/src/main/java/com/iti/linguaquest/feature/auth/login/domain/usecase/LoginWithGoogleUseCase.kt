package com.iti.linguaquest.feature.auth.login.domain.usecase

import com.iti.linguaquest.feature.auth.login.domain.repository.LoginRepository
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val repository: LoginRepository,
) {
    suspend operator fun invoke(
        idToken: String,
    ) = repository.loginWithGoogle(idToken)
}
