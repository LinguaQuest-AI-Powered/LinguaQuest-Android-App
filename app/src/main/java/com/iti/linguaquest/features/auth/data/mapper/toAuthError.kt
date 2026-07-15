package com.iti.linguaquest.features.auth.data.mapper

import com.iti.linguaquest.core.network.LinguaQuestDataError
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
}