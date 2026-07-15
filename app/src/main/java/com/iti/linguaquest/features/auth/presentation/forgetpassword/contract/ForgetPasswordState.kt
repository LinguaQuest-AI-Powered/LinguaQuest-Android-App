package com.iti.linguaquest.features.auth.presentation.forgetpassword.contract

data class ForgetPasswordState(
    val isLoading: Boolean = false,
    val emailError: Boolean = false,
    val emailErrorRes: Int? = null,
    val generalErrorRes: Int? = null,
)
