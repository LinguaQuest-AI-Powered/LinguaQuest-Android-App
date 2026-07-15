package com.iti.linguaquest.features.auth.presentation.login.mapper

import androidx.annotation.StringRes
import com.iti.linguaquest.R
import com.iti.linguaquest.features.auth.domain.model.AuthError

@StringRes
internal fun AuthError.toMessageRes(): Int = when (this) {
    AuthError.InvalidEmail -> R.string.login_error_invalid_email
    AuthError.InvalidCredentials -> R.string.login_error_invalid_credentials
    AuthError.EmailNotVerified -> R.string.login_error_email_not_verified
    AuthError.WeakPassword -> R.string.login_error_weak_password
    AuthError.UserDisabled -> R.string.login_error_user_disabled
    AuthError.TokenNotValid -> R.string.login_error_token_not_valid
    AuthError.OperationNotAllowed -> R.string.login_error_operation_not_allowed
    AuthError.EmailAlreadyInUse -> R.string.login_error_generic
    AuthError.Unknown -> R.string.login_error_generic
}