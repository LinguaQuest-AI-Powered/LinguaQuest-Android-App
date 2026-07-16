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
    data object EmailNotFound : AuthError
    data object UsernameAlreadyExists : AuthError
    data object InvalidOtp : AuthError
    data object OtpExpired : AuthError
    data object InvalidRefreshToken : AuthError
    data object RefreshTokenExpired : AuthError
    data object InvalidResetToken : AuthError
    data object ResetTokenExpired : AuthError
    data object InvalidIdToken : AuthError
    data object InvalidIdentityToken : AuthError
    data object ValidationError : AuthError
    data object EmailAlreadyExists : AuthError
    data object TooManyRequests : AuthError
    data object InternalServerError : AuthError
    data object Unknown : AuthError
}