package com.iti.linguaquest.core.network

import com.iti.linguaquest.core.cache.data.datasource.SessionManagerDataSource
import com.iti.linguaquest.core.cache.token.TokensLocalDataSource
import com.iti.linguaquest.core.session.SessionEvent
import com.iti.linguaquest.core.session.SessionEventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokensLocalDataSource: TokensLocalDataSource,
    private val sessionManagerDataSource: SessionManagerDataSource,
    private val sessionEventBus: SessionEventBus,
    private val applicationScope: CoroutineScope
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val invocation = request.tag(Invocation::class.java)
        val noAuth = invocation?.method()?.getAnnotation(NoAuth::class.java)

        if (noAuth == null) {
            val token = tokensLocalDataSource.getAccessTokenImmediate()
            if (!token.isNullOrBlank()) {
                request = request.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            }
        }

        val response = chain.proceed(request)

        if (noAuth == null && (response.code == 401 || response.code == 403)) {
            applicationScope.launch {
                tokensLocalDataSource.clearTokens()
                sessionManagerDataSource.saveIsLoggedIn(false)
                sessionManagerDataSource.clearSessionData()
                sessionEventBus.emit(SessionEvent.SessionExpired)
            }
        }

        return response
    }
}

