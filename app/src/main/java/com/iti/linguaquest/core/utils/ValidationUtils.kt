package com.iti.linguaquest.core.utils

import android.util.Patterns
import java.util.regex.Pattern

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        // Password should be at least 8 characters long, contain one uppercase, one lowercase and one number
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$"
        return password.isNotBlank() && Pattern.compile(passwordPattern).matcher(password).matches()
    }

    fun isValidName(name: String): Boolean {
        // Name should not be empty and should have at least 2 characters
        return name.isNotBlank() && name.length >= 2
    }
}
