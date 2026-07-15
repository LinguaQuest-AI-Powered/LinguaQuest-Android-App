package com.iti.linguaquest.features.auth.presentation.login.contract

sealed interface LoginIntent {
    data class EmailChanged(val email: String) : LoginIntent
    data class PasswordChanged(val password: String) : LoginIntent
    data object LoginClicked : LoginIntent
    data object GoogleSignInClicked : LoginIntent
    data class GoogleLoginSubmitted(val idToken: String) : LoginIntent
    data object GoogleSignInFailed : LoginIntent
    data object ForgetPasswordClicked : LoginIntent
    data object SignUpClicked : LoginIntent
}
