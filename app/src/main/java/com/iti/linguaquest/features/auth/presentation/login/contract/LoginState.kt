package com.iti.linguaquest.features.auth.presentation.login.contract

import com.iti.linguaquest.R

data class LoginState(
    val headerImageRes: Int = R.drawable.lingo,
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val googleError: Boolean = false,
    val generalErrorRes: Int? = null,
    val emailErrorRes: Int? = null,
    val passwordErrorRes: Int? = null,
)