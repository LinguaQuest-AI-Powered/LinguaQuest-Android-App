package com.iti.linguaquest.features.auth.presentation.forgetpassword.contract

sealed interface ForgetPasswordEffect {
    data object ShakeEmail : ForgetPasswordEffect
    data object SendSucceeded : ForgetPasswordEffect
    data object NavigateBackToLogin : ForgetPasswordEffect
}
