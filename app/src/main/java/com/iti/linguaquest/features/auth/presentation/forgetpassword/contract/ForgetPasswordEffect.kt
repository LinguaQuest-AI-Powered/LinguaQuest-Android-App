package com.iti.linguaquest.features.auth.presentation.forgetpassword.contract

sealed interface ForgetPasswordEffect {
    data object ShakeEmail : ForgetPasswordEffect
    data class SendSucceeded(val email: String) : ForgetPasswordEffect
    data object NavigateBackToLogin : ForgetPasswordEffect
}
