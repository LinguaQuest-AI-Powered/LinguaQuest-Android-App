package com.iti.linguaquest.features.auth.presentation.newpassword.contract

data class NewPasswordState(
    val isLoading: Boolean = false,
    val newPasswordError: Boolean = false,
    val confirmPasswordError: Boolean = false,
    val newPasswordErrorRes: Int? = null,
    val confirmPasswordErrorRes: Int? = null,
    val generalErrorRes: Int? = null,
)
