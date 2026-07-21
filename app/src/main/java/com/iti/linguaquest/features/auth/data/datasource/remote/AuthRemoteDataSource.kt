package com.iti.linguaquest.features.auth.data.datasource.remote

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult

interface AuthRemoteDataSource {
    suspend fun register(body: RegisterRequestDto): LinguaQuestResult<RegisterResponseDataDto, LinguaQuestDataError>
    suspend fun login(body: LoginRequestDto): LinguaQuestResult<LoginResponseDataDto, LinguaQuestDataError>
    suspend fun loginWithGoogle(body: OAuthGoogleRequestDto): LinguaQuestResult<OAuthResponseDataDto, LinguaQuestDataError>
    suspend fun sendOtp(body: OtpSendRequestDto): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun verifyEmailOtp(body: OtpVerifyRequestDto): LinguaQuestResult<VerifyEmailResponseDto, LinguaQuestDataError>
    suspend fun verifyPasswordResetOtp(body: OtpVerifyRequestDto): LinguaQuestResult<VerifyResetOtpResponseDto, LinguaQuestDataError>
    suspend fun setNewPassword(body: ResetPasswordRequestDto): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun logout(body: LogoutRequestDto): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun refreshToken(body: RefreshTokenRequestDto): LinguaQuestResult<RefreshTokenResponseDataDto, LinguaQuestDataError>
}