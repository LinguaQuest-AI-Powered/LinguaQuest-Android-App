package com.iti.linguaquest.features.auth.domain.model

import com.iti.linguaquest.core.network.AppError


sealed interface AuthError : AppError {
    data object InvalidEmail : AuthError
    data object InvalidCredentials : AuthError
    data object EmailNotVerified : AuthError
    data object WeakPassword : AuthError
    data object UserDisabled : AuthError
    data object TokenNotValid : AuthError
    data object OperationNotAllowed : AuthError
    data object EmailAlreadyInUse : AuthError
    data object Unknown : AuthError
}