package com.iti.linguaquest.features.auth.presentation.otp.contract

sealed interface OTPIntent {
    data class OnOtpCodeChanged(val code: String) : OTPIntent
    object OnVerifyClicked : OTPIntent
    object OnResendCodeClicked : OTPIntent
    object OnBackClicked : OTPIntent
    object OnBackToLoginClicked : OTPIntent
}
