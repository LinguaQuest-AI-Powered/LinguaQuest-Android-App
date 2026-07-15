package com.iti.linguaquest.features.auth.presentation.newpassword.contract

sealed interface NewPasswordEffect {
    data object ShakeNewPassword : NewPasswordEffect
    data object ShakeConfirmPassword : NewPasswordEffect
    data object ResetSucceeded : NewPasswordEffect
    data object NavigateBackToLogin : NewPasswordEffect
}
