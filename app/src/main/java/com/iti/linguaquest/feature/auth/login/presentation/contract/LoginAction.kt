package com.iti.linguaquest.feature.auth.login.presentation.contract

sealed interface LoginAction {
    data class PasswordChanged(val value: String) : LoginAction
    object TogglePasswordVisibility : LoginAction
    object LoginClicked : LoginAction
    data class GoogleLoginSubmitted(val idToken: String) : LoginAction
    object GoogleSignInFailed : LoginAction
    object ForgetPassword : LoginAction
    object ContinueAsGuestClicked : LoginAction
}