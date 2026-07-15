package com.iti.linguaquest.features.auth.presentation.signup.contract
import com.iti.linguaquest.R

data class SignUpState(
    val headerImageRes: Int = R.drawable.lingo_register,
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
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
