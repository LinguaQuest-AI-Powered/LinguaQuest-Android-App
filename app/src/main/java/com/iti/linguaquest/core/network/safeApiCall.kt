package com.iti.linguaquest.core.network

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


suspend inline fun <T> safeApiCall(
    noinline mapServerError: (errorKey: String, errorMessage: String) -> LinguaQuestDataError =
        { _, message -> LinguaQuestDataError.CustomServerMessage(message) },
    crossinline apiCall: suspend () -> T
): LinguaQuestResult<T, LinguaQuestDataError> {
    return try {
        LinguaQuestResult.Success(apiCall())
    } catch (e: UnknownHostException) {
        LinguaQuestResult.Failure(LinguaQuestDataError.Remote.NO_INTERNET)
    } catch (e: SocketTimeoutException) {
        LinguaQuestResult.Failure(LinguaQuestDataError.Remote.REQUEST_TIMEOUT)
    } catch (e: HttpException) {
        LinguaQuestResult.Failure(e.toLinguaQuestDataError(mapServerError))
    } catch (e: JsonSyntaxException) {
        LinguaQuestResult.Failure(LinguaQuestDataError.Remote.SERIALIZATION)
    } catch (e: IOException) {
        LinguaQuestResult.Failure(LinguaQuestDataError.Remote.NO_INTERNET)
    } catch (e: Exception) {
        LinguaQuestResult.Failure(LinguaQuestDataError.Remote.UNKNOWN)
    }
}

fun HttpException.toLinguaQuestDataError(
    mapServerError: (errorKey: String, errorMessage: String) -> LinguaQuestDataError
): LinguaQuestDataError {
    val rawBody = response()?.errorBody()?.string()

    val parsed = rawBody?.let {
        try {
            Gson().fromJson(it, ErrorResponseDto::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    val errorKey = parsed?.error?.errorKey
    val errorMessage = parsed?.error?.errorMessage ?: "Unknown error"

    return errorKey?.let { key -> mapServerError(key, errorMessage) }
        ?: code().toGenericLinguaQuestDataError()
}

private fun Int.toGenericLinguaQuestDataError(): LinguaQuestDataError.Remote = when (this) {
    400 -> LinguaQuestDataError.Remote.BAD_REQUEST
    401, 403 -> LinguaQuestDataError.Remote.UNAUTHORIZED
    408 -> LinguaQuestDataError.Remote.REQUEST_TIMEOUT
    429 -> LinguaQuestDataError.Remote.TOO_MANY_REQUESTS
    in 500..599 -> LinguaQuestDataError.Remote.SERVER
    else -> LinguaQuestDataError.Remote.UNKNOWN
}