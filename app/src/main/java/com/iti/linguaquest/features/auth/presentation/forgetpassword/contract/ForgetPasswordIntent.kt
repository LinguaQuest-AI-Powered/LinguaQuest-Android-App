package com.iti.linguaquest.features.auth.presentation.forgetpassword.contract

sealed interface ForgetPasswordIntent {
    data class SendClicked(val email: String) : ForgetPasswordIntent
    data object BackToLoginClicked : ForgetPasswordIntent
}
