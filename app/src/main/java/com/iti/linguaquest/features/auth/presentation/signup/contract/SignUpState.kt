package com.iti.linguaquest.features.auth.presentation.signup.contract

data class SignUpState(
    val isLoading: Boolean = false,
    val usernameError: Boolean = false,
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val confirmPasswordError: Boolean = false,
    val googleError: Boolean = false,
    val generalErrorRes: Int? = null,
    val usernameErrorRes: Int? = null,
    val emailErrorRes: Int? = null,
    val passwordErrorRes: Int? = null,
    val confirmPasswordErrorRes: Int? = null,
)
