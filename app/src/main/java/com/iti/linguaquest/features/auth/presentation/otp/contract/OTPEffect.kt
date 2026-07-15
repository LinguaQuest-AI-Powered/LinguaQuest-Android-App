package com.iti.linguaquest.features.auth.presentation.otp.contract

sealed interface OTPEffect {
    object NavigateBack : OTPEffect
    object NavigateToLogin : OTPEffect
    object NavigateToNextScreen : OTPEffect
    data class ShowError(val message: String) : OTPEffect
}