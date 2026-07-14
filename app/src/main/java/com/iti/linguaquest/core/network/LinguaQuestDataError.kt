package com.iti.linguaquest.core.network


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

    data class CustomServerMessage(
        val message: String
    ) : LinguaQuestDataError
}