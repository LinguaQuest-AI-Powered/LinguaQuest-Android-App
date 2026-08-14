package com.iti.linguaquest.features.auth.presentation.login.mapper

import androidx.annotation.StringRes
import com.iti.linguaquest.R
import com.iti.linguaquest.features.auth.domain.model.AuthError

@StringRes
fun AuthError.toMessageRes(): Int = when (this) {
    AuthError.InvalidEmail -> R.string.login_error_invalid_email
    AuthError.InvalidCredentials -> R.string.login_error_invalid_credentials
    AuthError.InvalidPassword -> R.string.login_error_invalid_password
    AuthError.EmailNotVerified -> R.string.login_error_email_not_verified
    AuthError.WeakPassword -> R.string.login_error_weak_password
    AuthError.UserDisabled -> R.string.login_error_user_disabled
    AuthError.TokenNotValid -> R.string.login_error_token_not_valid
    AuthError.OperationNotAllowed -> R.string.login_error_operation_not_allowed
    AuthError.EmailAlreadyInUse -> R.string.login_error_email_already_in_use
    AuthError.EmailAlreadyExists -> R.string.login_error_email_already_in_use
    AuthError.EmailNotFound -> R.string.login_error_email_not_found
    AuthError.UsernameAlreadyExists -> R.string.login_error_username_already_exists
    AuthError.InvalidOtp -> R.string.otp_error_invalid
    AuthError.OtpExpired -> R.string.otp_error_expired
    AuthError.OtpNotFound -> R.string.otp_error_expired
    AuthError.InvalidRefreshToken -> R.string.login_error_token_not_valid
    AuthError.RefreshTokenExpired -> R.string.login_error_token_not_valid
    AuthError.InvalidResetToken -> R.string.login_error_invalid_reset_token
    AuthError.ResetTokenExpired -> R.string.login_error_reset_token_expired
    AuthError.InvalidIdToken -> R.string.login_error_invalid_credentials
    AuthError.InvalidIdentityToken -> R.string.login_error_invalid_credentials
    AuthError.ValidationError -> R.string.login_error_validation_error
    AuthError.TooManyRequests -> R.string.error_too_many_requests
    AuthError.InternalServerError -> R.string.error_server
    AuthError.Unknown -> R.string.login_error_generic
}