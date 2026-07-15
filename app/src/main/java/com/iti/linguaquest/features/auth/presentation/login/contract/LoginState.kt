package com.iti.linguaquest.features.auth.presentation.login.contract


data class LoginState(
    val isLoading: Boolean = false,
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val googleError: Boolean = false,
    val generalErrorRes: Int? = null,
    val emailErrorRes: Int? = null,
    val passwordErrorRes: Int? = null,
)