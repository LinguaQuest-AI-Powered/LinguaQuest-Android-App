package com.iti.linguaquest.features.auth.data.datasource

import com.iti.linguaquest.core.network.SuccessResponseDto
import retrofit2.http.Body
 import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/register")
    suspend fun register(
        @Body body: RegisterRequestDto
    ): SuccessResponseDto<RegisterResponseDataDto>

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequestDto
    ): SuccessResponseDto<LoginResponseDataDto>

    @POST("auth/oauth/google")
    suspend fun loginWithGoogle(
        @Body body: OAuthGoogleRequestDto
    ): SuccessResponseDto<OAuthResponseDataDto>

    @POST("auth/send-otp")
    suspend fun sendRegistrationOtp(
        @Body body: EmailRequestDto
    ): SuccessResponseDto<Unit>


}