package com.iti.linguaquest.features.auth.presentation.newpassword.contract

sealed interface NewPasswordIntent {
    data class ResetPasswordClicked(
        val resetToken: String = "",
        val newPassword: String,
        val confirmPassword: String,
    ) : NewPasswordIntent
    data object BackToLoginClicked : NewPasswordIntent
}
