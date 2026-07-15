package com.iti.linguaquest.features.auth.presentation.otp.contract

data class OTPState(
    val otpCode: String = "",
    val timerText: String = "01:30",
    val isTimerActive: Boolean = true,
    val isLoading: Boolean = false,
    val isVerifyEnabled: Boolean = false
)