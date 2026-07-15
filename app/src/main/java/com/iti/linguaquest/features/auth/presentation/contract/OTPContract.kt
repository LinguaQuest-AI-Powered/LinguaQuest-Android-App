package com.iti.linguaquest.features.auth.presentation.contract

sealed interface OTPIntent {
    data class OnOtpCodeChanged(val code: String) : OTPIntent
    object OnVerifyClicked : OTPIntent
    object OnResendCodeClicked : OTPIntent
    object OnBackClicked : OTPIntent
    object OnBackToLoginClicked : OTPIntent
}

data class OTPState(
    val otpCode: String = "",
    val timerText: String = "01:30",
    val isTimerActive: Boolean = true,
    val isLoading: Boolean = false,
    val isVerifyEnabled: Boolean = false
)

sealed interface OTPEffect {
    object NavigateBack : OTPEffect
    object NavigateToLogin : OTPEffect
    object NavigateToNextScreen : OTPEffect
    data class ShowError(val message: String) : OTPEffect
}