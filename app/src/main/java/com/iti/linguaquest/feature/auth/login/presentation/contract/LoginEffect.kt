package com.iti.linguaquest.features.auth.login.presentation.contract

sealed interface LoginEffect {
    data object ShakeEmail : LoginEffect
    data object ShakePassword : LoginEffect
    data object ShakeGoogleSignIn : LoginEffect
    data object LaunchGoogleSignIn : LoginEffect
    data object LoginSucceeded : LoginEffect
    data object NavigateToForgotPassword : LoginEffect
    data object NavigateToSignUp : LoginEffect
}
