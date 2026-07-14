package com.iti.linguaquest.core.network

sealed interface LinguaQuestResult<out D, out E : AppError> {

    data class Success<out D>(
        val data: D
    ) : LinguaQuestResult<D, Nothing>

    data class Failure<out E : AppError>(
        val error: E
    ) : LinguaQuestResult<Nothing, E>
}

inline fun <T, E : AppError, R> LinguaQuestResult<T, E>.map(
    transform: (T) -> R
): LinguaQuestResult<R, E> {
    return when (this) {
        is LinguaQuestResult.Success -> LinguaQuestResult.Success(transform(data))
        is LinguaQuestResult.Failure -> LinguaQuestResult.Failure(error)
    }
}

fun <T, E : AppError> LinguaQuestResult<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { Unit }
}

inline fun <T, E : AppError> LinguaQuestResult<T, E>.onSuccess(
    action: (T) -> Unit
): LinguaQuestResult<T, E> {
    return when (this) {
        is LinguaQuestResult.Success -> {
            action(data)
            this
        }

        is LinguaQuestResult.Failure -> this
    }
}

inline fun <T, E : AppError> LinguaQuestResult<T, E>.onFailure(
    action: (E) -> Unit
): LinguaQuestResult<T, E> {
    return when (this) {
        is LinguaQuestResult.Success -> this

        is LinguaQuestResult.Failure -> {
            action(error)
            this
        }
    }
}

typealias EmptyResult<E> = LinguaQuestResult<Unit, E>