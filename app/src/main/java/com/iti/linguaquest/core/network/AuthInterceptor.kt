package com.iti.linguaquest.core.network

import com.iti.linguaquest.core.cache.token.TokensLocalDataSource
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokensLocalDataSource: TokensLocalDataSource
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

        return chain.proceed(request)
    }
}
