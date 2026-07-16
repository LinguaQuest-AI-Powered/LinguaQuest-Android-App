package com.iti.linguaquest.features.auth.data.datasource.remote

import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.core.network.LinguaQuestResult

interface AuthRemoteDataSource {
    suspend fun register(body: RegisterRequestDto): LinguaQuestResult<RegisterResponseDataDto, LinguaQuestDataError>
    suspend fun login(body: LoginRequestDto): LinguaQuestResult<LoginResponseDataDto, LinguaQuestDataError>
    suspend fun loginWithGoogle(body: OAuthGoogleRequestDto): LinguaQuestResult<OAuthResponseDataDto, LinguaQuestDataError>
    suspend fun sendOtp(body: OtpSendRequestDto): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun verifyEmailOtp(body: OtpVerifyRequestDto): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun verifyPasswordResetOtp(body: OtpVerifyRequestDto): LinguaQuestResult<VerifyResetOtpResponseDto, LinguaQuestDataError>
    suspend fun setNewPassword(body: ResetPasswordRequestDto): LinguaQuestResult<Unit, LinguaQuestDataError>
}