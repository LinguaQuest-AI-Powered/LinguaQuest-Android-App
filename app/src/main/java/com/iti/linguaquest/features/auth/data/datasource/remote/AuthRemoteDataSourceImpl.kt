package com.iti.linguaquest.features.auth.data.datasource.remote

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.network.safeApiCall
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val api: AuthApiService
) : AuthRemoteDataSource {

    private val mapAuthError: (String, String) -> LinguaQuestDataError = { key, msg ->
        try {
            LinguaQuestDataError.Auth.valueOf(key)
        } catch (e: IllegalArgumentException) {
            LinguaQuestDataError.CustomServerMessage(msg)
        }
    }

    override suspend fun register(body: RegisterRequestDto): LinguaQuestResult<RegisterResponseDataDto, LinguaQuestDataError> =
        safeApiCall(mapAuthError) { api.register(body).data }

    override suspend fun login(body: LoginRequestDto): LinguaQuestResult<LoginResponseDataDto, LinguaQuestDataError> =
        safeApiCall(mapAuthError) { api.login(body).data }

    override suspend fun loginWithGoogle(body: OAuthGoogleRequestDto): LinguaQuestResult<OAuthResponseDataDto, LinguaQuestDataError> =
        safeApiCall(mapAuthError) { api.loginWithGoogle(body).data }

    override suspend fun completeOAuthProfile(body: CompleteProfileRequestDto): LinguaQuestResult<OAuthResponseDataDto, LinguaQuestDataError> =
        safeApiCall(mapAuthError) { api.completeOAuthProfile(body).data }

    override suspend fun sendOtp(body: OtpSendRequestDto): LinguaQuestResult<Unit, LinguaQuestDataError> =
        safeApiCall(mapAuthError) { api.sendOtp(body).data }

    override suspend fun verifyEmailOtp(body: OtpVerifyRequestDto): LinguaQuestResult<VerifyEmailResponseDto, LinguaQuestDataError> =
        safeApiCall(mapAuthError) { api.verifyEmailOtp(body).data }

    override suspend fun verifyPasswordResetOtp(body: OtpVerifyRequestDto): LinguaQuestResult<VerifyResetOtpResponseDto, LinguaQuestDataError> =
        safeApiCall(mapAuthError) { api.verifyPasswordResetOtp(body).data }

    override suspend fun setNewPassword(body: ResetPasswordRequestDto): LinguaQuestResult<Unit, LinguaQuestDataError> =
        safeApiCall(mapAuthError) { api.setNewPassword(body).data }

    override suspend fun logout(body: LogoutRequestDto): LinguaQuestResult<Unit, LinguaQuestDataError> =
        safeApiCall(mapAuthError) { api.logout(body).data }

    override suspend fun refreshToken(body: RefreshTokenRequestDto): LinguaQuestResult<RefreshTokenResponseDataDto, LinguaQuestDataError> =
        safeApiCall(mapAuthError) { api.refreshToken(body).data }

}