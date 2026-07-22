package com.iti.linguaquest.features.auth.presentation.signup.contract

sealed interface SignUpIntent {
    data class SignUpClicked(
        val username: String,
        val email: String,
        val password: String,
        val confirmPassword: String
    ) : SignUpIntent
    data object LoginClicked : SignUpIntent
    data object GoogleSignInClicked : SignUpIntent
    data class GoogleLoginSubmitted(val idToken: String) : SignUpIntent
    data object GoogleSignInFailed : SignUpIntent
    data object OAuthLanguageSelectionCompleted : SignUpIntent
}
