package com.iti.linguaquest.core.di

import com.iti.linguaquest.BuildConfig
import com.iti.linguaquest.core.network.NetworkConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.iti.linguaquest.core.network.AuthInterceptor
import com.iti.linguaquest.core.network.TokenAuthenticator
import com.iti.linguaquest.core.ai.network.GeminiApiService
import com.iti.linguaquest.core.ai.network.ItiGatewayApiService
import com.iti.linguaquest.core.language.data.datasource.remote.LanguageApiService
import com.iti.linguaquest.features.auth.data.datasource.remote.AuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor {
                it.proceed(
                    it.request().newBuilder()
                        .build()
                )
            }
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetworkConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(
        retrofit: Retrofit
    ): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLanguageApiService(
        retrofit: Retrofit
    ): LanguageApiService {
        return retrofit.create(LanguageApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGeminiApiService(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): GeminiApiService {
        return Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(GeminiApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideItiGatewayApiService(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): ItiGatewayApiService {
        val rawBaseUrl = BuildConfig.AI_BASE_URL.ifBlank { "http://apiaccess.iti.net.eg/api/v1/student/" }
        val normalizedBaseUrl = if (rawBaseUrl.endsWith("/")) rawBaseUrl else "$rawBaseUrl/"
        return Retrofit.Builder()
            .baseUrl(normalizedBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ItiGatewayApiService::class.java)
    }
}
