package com.iti.linguaquest.core.utils

import android.util.Patterns
import androidx.annotation.StringRes
import com.iti.linguaquest.R
import java.util.regex.Pattern

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return getPasswordValidationErrorRes(password) == null
    }

    @StringRes
    fun getPasswordValidationErrorRes(
        password: String,
        @StringRes blankErrorRes: Int = R.string.login_error_password_required
    ): Int? {
        return when {
            password.isBlank() -> blankErrorRes
            password.length < 8 -> R.string.new_password_error_too_short
            !password.any { it.isUpperCase() } -> R.string.new_password_error_no_uppercase
            !password.any { it.isDigit() } -> R.string.new_password_error_no_number
            else -> null
        }
    }

    fun isValidName(name: String): Boolean {
        // Name should not be empty and should have at least 2 characters
        return name.isNotBlank() && name.length >= 2
    }
}
