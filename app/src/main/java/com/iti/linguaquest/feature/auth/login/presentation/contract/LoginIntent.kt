package com.iti.linguaquest.features.auth.login.presentation.contract

sealed interface LoginIntent {
    data class EmailChanged(val value: String) : LoginIntent
    data class PasswordChanged(val value: String) : LoginIntent
    data object TogglePasswordVisibility : LoginIntent
    data object LoginClicked : LoginIntent
    data object GoogleSignInClicked : LoginIntent
    data class GoogleLoginSubmitted(val idToken: String) : LoginIntent
    data object GoogleSignInFailed : LoginIntent
    data object ForgetPasswordClicked : LoginIntent
    data object ContinueAsGuestClicked : LoginIntent
    data object SignUpClicked : LoginIntent
}
