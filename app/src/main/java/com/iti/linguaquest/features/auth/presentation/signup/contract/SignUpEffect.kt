package com.iti.linguaquest.features.auth.presentation.signup.contract

sealed interface SignUpEffect {
    data object SignUpSucceeded : SignUpEffect
    data object NavigateToLogin : SignUpEffect
    data object ShakeGoogleSignIn : SignUpEffect
    data object LaunchGoogleSignIn : SignUpEffect
    data object ShakeUsername : SignUpEffect
    data object ShakeEmail : SignUpEffect
    data object ShakePassword : SignUpEffect
    data object ShakeConfirmPassword : SignUpEffect
}
