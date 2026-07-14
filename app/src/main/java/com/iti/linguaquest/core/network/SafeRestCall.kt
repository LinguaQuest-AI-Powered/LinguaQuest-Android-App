package com.iti.linguaquest.core.network

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): LinguaQuestResult<T, LinguaQuestDataError.Remote> =
    try {
        LinguaQuestResult.Success(apiCall())
    } catch (e: SocketTimeoutException) {
         LinguaQuestResult.Failure(LinguaQuestDataError.Remote.REQUEST_TIMEOUT)

    } catch (e: UnknownHostException) {
         LinguaQuestResult.Failure(LinguaQuestDataError.Remote.NO_INTERNET)

    } catch (e: HttpException) {
         httpToResult(e.code())

    } catch (e: SerializationException) {
         LinguaQuestResult.Failure(LinguaQuestDataError.Remote.SERIALIZATION)

    } catch (e: IOException) {
         LinguaQuestResult.Failure(LinguaQuestDataError.Remote.NO_INTERNET)

    } catch (e: CancellationException) {
        throw e

    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
         LinguaQuestResult.Failure(LinguaQuestDataError.Remote.UNKNOWN)
    }

private fun httpToResult(
    statusCode: Int
): LinguaQuestResult<Nothing, LinguaQuestDataError.Remote> =
    when (statusCode) {
        400 -> LinguaQuestResult.Failure(LinguaQuestDataError.Remote.BAD_REQUEST)
        401 -> LinguaQuestResult.Failure(LinguaQuestDataError.Remote.UNAUTHORIZED)
        403 -> LinguaQuestResult.Failure(LinguaQuestDataError.Remote.UNAUTHORIZED)
        404 -> LinguaQuestResult.Failure(LinguaQuestDataError.Remote.EMPTY_RESULT)
        408 -> LinguaQuestResult.Failure(LinguaQuestDataError.Remote.REQUEST_TIMEOUT)
        409 -> LinguaQuestResult.Failure(LinguaQuestDataError.Remote.BAD_REQUEST)
        422 -> LinguaQuestResult.Failure(LinguaQuestDataError.Remote.BAD_REQUEST)
        429 -> LinguaQuestResult.Failure(LinguaQuestDataError.Remote.TOO_MANY_REQUESTS)
        in 500..599 -> LinguaQuestResult.Failure(LinguaQuestDataError.Remote.SERVER)
        else -> LinguaQuestResult.Failure(LinguaQuestDataError.Remote.UNKNOWN)
    }