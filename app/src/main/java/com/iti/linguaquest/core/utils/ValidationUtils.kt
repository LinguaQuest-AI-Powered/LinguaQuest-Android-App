package com.iti.linguaquest.core.utils

import android.util.Patterns
import androidx.annotation.StringRes
import com.iti.linguaquest.R
import java.util.regex.Pattern

object ValidationUtils {

    private val STRICT_EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
    )

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && STRICT_EMAIL_PATTERN.matcher(email).matches()
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

    private val USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$")

    fun isValidName(name: String): Boolean {
        return getUsernameValidationErrorRes(name) == null
    }

    @StringRes
    fun getUsernameValidationErrorRes(name: String): Int? {
        return when {
            name.isBlank() -> R.string.signup_error_name_required
            name.length < 3 -> R.string.signup_error_name_too_short
            name.length > 30 -> R.string.signup_error_name_too_long
            !USERNAME_PATTERN.matcher(name).matches() -> R.string.signup_error_name_invalid_chars
            else -> null
        }
    }
}
