package com.iti.linguaquest.features.auth.data.mapper

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.features.auth.domain.model.AuthError


fun LinguaQuestDataError.Auth.toAuthError(): AuthError = when (this) {
    LinguaQuestDataError.Auth.INVALID_EMAIL -> AuthError.InvalidEmail
    LinguaQuestDataError.Auth.INVALID_CREDENTIALS -> AuthError.InvalidCredentials
    LinguaQuestDataError.Auth.EMAIL_NOT_VERIFIED -> AuthError.EmailNotVerified
    LinguaQuestDataError.Auth.WEAK_PASSWORD -> AuthError.WeakPassword
    LinguaQuestDataError.Auth.USER_DISABLED -> AuthError.UserDisabled
    LinguaQuestDataError.Auth.TOKEN_NOT_VALID -> AuthError.TokenNotValid
    LinguaQuestDataError.Auth.OPERATION_NOT_ALLOWED -> AuthError.OperationNotAllowed
    LinguaQuestDataError.Auth.EMAIL_ALREADY_IN_USE -> AuthError.EmailAlreadyInUse
    LinguaQuestDataError.Auth.UNKNOWN -> AuthError.Unknown
    LinguaQuestDataError.Auth.EMAIL_NOT_FOUND -> AuthError.EmailNotFound
    LinguaQuestDataError.Auth.USERNAME_ALREADY_EXISTS -> AuthError.UsernameAlreadyExists
    LinguaQuestDataError.Auth.INVALID_OTP -> AuthError.InvalidOtp
    LinguaQuestDataError.Auth.OTP_EXPIRED -> AuthError.OtpExpired
    LinguaQuestDataError.Auth.INVALID_REFRESH_TOKEN -> AuthError.InvalidRefreshToken
    LinguaQuestDataError.Auth.REFRESH_TOKEN_EXPIRED -> AuthError.RefreshTokenExpired
    LinguaQuestDataError.Auth.INVALID_RESET_TOKEN -> AuthError.InvalidResetToken
    LinguaQuestDataError.Auth.RESET_TOKEN_EXPIRED -> AuthError.ResetTokenExpired
    LinguaQuestDataError.Auth.INVALID_ID_TOKEN -> AuthError.InvalidIdToken
    LinguaQuestDataError.Auth.INVALID_IDENTITY_TOKEN -> AuthError.InvalidIdentityToken
    LinguaQuestDataError.Auth.VALIDATION_ERROR -> AuthError.ValidationError
    LinguaQuestDataError.Auth.EMAIL_ALREADY_EXISTS -> AuthError.EmailAlreadyExists
    LinguaQuestDataError.Auth.TOO_MANY_REQUESTS -> AuthError.TooManyRequests
    LinguaQuestDataError.Auth.INTERNAL_SERVER_ERROR -> AuthError.InternalServerError
}