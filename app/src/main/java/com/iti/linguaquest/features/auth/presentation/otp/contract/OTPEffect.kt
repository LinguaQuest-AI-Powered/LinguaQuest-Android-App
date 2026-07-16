package com.iti.linguaquest.features.auth.presentation.otp.contract

sealed interface OTPEffect {
    object NavigateBack : OTPEffect
    object NavigateToLogin : OTPEffect
    data class NavigateToNextScreen(val resetToken: String?) : OTPEffect
    data class ShowError(@androidx.annotation.StringRes val messageRes: Int) : OTPEffect
}