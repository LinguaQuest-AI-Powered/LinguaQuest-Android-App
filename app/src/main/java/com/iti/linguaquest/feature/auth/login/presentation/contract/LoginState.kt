package com.iti.linguaquest.feature.auth.login.presentation.contract

import com.iti.linguaquest.core.network.LinguaQuestDataError

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val generalError: LinguaQuestDataError? = null,
)