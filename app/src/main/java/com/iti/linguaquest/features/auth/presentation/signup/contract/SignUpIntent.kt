package com.iti.linguaquest.features.auth.presentation.signup.contract

sealed interface SignUpIntent {
    data class UsernameChanged(val username: String) : SignUpIntent
    data class EmailChanged(val email: String) : SignUpIntent
    data class PasswordChanged(val password: String) : SignUpIntent
    data class ConfirmPasswordChanged(val password: String) : SignUpIntent
    data object SignUpClicked : SignUpIntent
    data object LoginClicked : SignUpIntent
    data object GoogleSignInClicked : SignUpIntent
    data class GoogleLoginSubmitted(val idToken: String) : SignUpIntent
    data object GoogleSignInFailed : SignUpIntent
}
