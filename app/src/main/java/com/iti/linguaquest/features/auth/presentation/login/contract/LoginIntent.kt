package com.iti.linguaquest.features.auth.presentation.login.contract

sealed interface LoginIntent {
    data class LoginClicked(val email: String, val password: String) : LoginIntent
    data object GoogleSignInClicked : LoginIntent
    data class GoogleLoginSubmitted(val idToken: String) : LoginIntent
    data object GoogleSignInFailed : LoginIntent
    data object ForgetPasswordClicked : LoginIntent
    data object SignUpClicked : LoginIntent
    data object OAuthLanguageSelectionCompleted : LoginIntent
}
