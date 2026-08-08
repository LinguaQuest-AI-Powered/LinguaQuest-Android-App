package com.iti.linguaquest.core.network

import com.google.gson.Gson
import com.google.gson.JsonParser.parseString
import com.iti.linguaquest.core.cache.data.datasource.SessionManagerDataSource
import com.iti.linguaquest.core.cache.token.TokensLocalDataSource
import com.iti.linguaquest.core.session.SessionEvent
import com.iti.linguaquest.core.session.SessionEventBus
import com.iti.linguaquest.features.auth.data.datasource.remote.RefreshTokenRequestDto
import com.iti.linguaquest.features.auth.data.datasource.remote.RefreshTokenResponseDataDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val tokensLocalDataSource: TokensLocalDataSource,
    private val sessionManagerDataSource: SessionManagerDataSource,
    private val sessionEventBus: SessionEventBus,
    private val applicationScope: CoroutineScope,
    private val okHttpClientProvider: Provider<OkHttpClient>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.url.encodedPath.contains("auth/refresh-token")) {
            applicationScope.launch {
                tokensLocalDataSource.clearTokens()
                sessionManagerDataSource.saveIsLoggedIn(false)
                sessionManagerDataSource.clearSessionData()
                sessionEventBus.emit(SessionEvent.SessionExpired)
            }
            return null
        }

        return synchronized(this) {
            val currentToken = tokensLocalDataSource.getAccessTokenImmediate()
            if (response.request.header("Authorization") != "Bearer $currentToken") {
                return@synchronized response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            val newAccessToken = runBlocking { refreshTokens() }

            if (newAccessToken != null) {
                response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            } else {
                applicationScope.launch {
                    tokensLocalDataSource.clearTokens()
                    sessionManagerDataSource.saveIsLoggedIn(false)
                    sessionManagerDataSource.clearSessionData()
                    sessionEventBus.emit(SessionEvent.SessionExpired)
                }
                null
            }
        }
    }

    private suspend fun refreshTokens(): String? {
        val refreshToken = tokensLocalDataSource.refreshToken.firstOrNull() ?: return null
        
        val requestDto = RefreshTokenRequestDto(refreshToken)
        val requestBody = Gson().toJson(requestDto).toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("${NetworkConfig.BASE_URL}auth/refresh-token")
            .post(requestBody)
            .build()

        try {
            val response = okHttpClientProvider.get().newCall(request).execute()
            if (response.isSuccessful) {
                val responseBodyStr = response.body?.string() ?: return null
                val jsonObject = parseString(responseBodyStr).asJsonObject
                if (jsonObject.has("data")) {
                    val dataObj = jsonObject.getAsJsonObject("data")
                    val data = Gson().fromJson(dataObj, RefreshTokenResponseDataDto::class.java)
                    tokensLocalDataSource.saveTokens(data.accessToken, data.refreshToken)
                    return data.accessToken
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to refresh token")
        }

        return null
    }
}

