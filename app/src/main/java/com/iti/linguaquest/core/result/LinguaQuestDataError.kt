package com.iti.linguaquest.core.result


sealed interface LinguaQuestDataError : AppError {

    enum class Remote : LinguaQuestDataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        BAD_REQUEST,
        UNAUTHORIZED,
        SERVER,
        SERIALIZATION,
        UNKNOWN,
        EMPTY_RESULT
    }

    enum class Auth : LinguaQuestDataError {
        INVALID_EMAIL,
        INVALID_CREDENTIALS,
        EMAIL_ALREADY_IN_USE,
        WEAK_PASSWORD,
        USER_DISABLED,
        EMAIL_NOT_VERIFIED,
        TOKEN_NOT_VALID,
        OPERATION_NOT_ALLOWED,

        EMAIL_NOT_FOUND,
        USERNAME_ALREADY_EXISTS,
        INVALID_OTP,
        OTP_EXPIRED,
        INVALID_REFRESH_TOKEN,
        REFRESH_TOKEN_EXPIRED,
        INVALID_RESET_TOKEN,
        RESET_TOKEN_EXPIRED,
        INVALID_ID_TOKEN,
        INVALID_IDENTITY_TOKEN,
        VALIDATION_ERROR,
        EMAIL_ALREADY_EXISTS,
        TOO_MANY_REQUESTS,
        INTERNAL_SERVER_ERROR,
        UNKNOWN
    }

    enum class Firestore : LinguaQuestDataError {
        PERMISSION_DENIED,
        NOT_FOUND,
        ALREADY_EXISTS,
        UNAVAILABLE,
        QUOTA_EXCEEDED,
        DATA_LOSS,
        CANCELLED
    }
    enum class Local : LinguaQuestDataError {
        DISK_FULL,
        NOT_FOUND,
        CONSTRAINT_VIOLATION,
        UNKNOWN
    }

    data class CustomServerMessage(
        val message: String
    ) : LinguaQuestDataError
}