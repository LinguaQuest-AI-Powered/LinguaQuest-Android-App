package com.iti.linguaquest.features.auth.presentation.otp.contract

sealed interface OTPEffect {
    object NavigateBack : OTPEffect
    object NavigateToLogin : OTPEffect
    data class NavigateToNextScreen(val resetToken: String?) : OTPEffect
}