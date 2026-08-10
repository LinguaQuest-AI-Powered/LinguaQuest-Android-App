package com.iti.linguaquest.features.auth.data.datasource.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.core.network.NoAuth
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApiService {

    @NoAuth
    @POST("auth/register")
    suspend fun register(
        @Body body: RegisterRequestDto
    ): SuccessResponseDto<RegisterResponseDataDto>

    @NoAuth
    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequestDto
    ): SuccessResponseDto<LoginResponseDataDto>

    @NoAuth
    @POST("auth/oauth/firebase")
    suspend fun loginWithGoogle(
        @Body body: OAuthGoogleRequestDto
    ): SuccessResponseDto<OAuthResponseDataDto>

    @POST("profile/complete-profile")
    suspend fun completeOAuthProfile(
        @Body body: CompleteProfileRequestDto
    ): SuccessResponseDto<OAuthResponseDataDto>

    @NoAuth
    @POST("auth/otp/send")
    suspend fun sendOtp(
        @Body body: OtpSendRequestDto
    ): SuccessResponseDto<Unit>

    @NoAuth
    @POST("auth/otp/verify")
    suspend fun verifyEmailOtp(
        @Body body: OtpVerifyRequestDto
    ): SuccessResponseDto<VerifyEmailResponseDto>

    @NoAuth
    @POST("auth/forget-password/otp/verify")
    suspend fun verifyPasswordResetOtp(
        @Body body: OtpVerifyRequestDto
    ): SuccessResponseDto<VerifyResetOtpResponseDto>

    @NoAuth
    @PATCH("auth/forget-password")
    suspend fun setNewPassword(
        @Body body: ResetPasswordRequestDto
    ): SuccessResponseDto<Unit>

    @POST("auth/logout")
    suspend fun logout(
        @Body body: LogoutRequestDto
    ): SuccessResponseDto<Unit>

    @NoAuth
    @POST("auth/refresh-token")
    suspend fun refreshToken(
        @Body body: RefreshTokenRequestDto
    ): SuccessResponseDto<RefreshTokenResponseDataDto>
}