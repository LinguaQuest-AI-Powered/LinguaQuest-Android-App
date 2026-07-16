package com.iti.linguaquest.features.auth.data.datasource

import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.core.network.safeApiCall
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val api: AuthApiService
) : AuthRemoteDataSource {

    override suspend fun register(body: RegisterRequestDto): LinguaQuestResult<RegisterResponseDataDto, LinguaQuestDataError> =
        safeApiCall { api.register(body).data }

    override suspend fun login(body: LoginRequestDto): LinguaQuestResult<LoginResponseDataDto, LinguaQuestDataError> =
        safeApiCall { api.login(body).data }

    override suspend fun loginWithGoogle(body: OAuthGoogleRequestDto): LinguaQuestResult<OAuthResponseDataDto, LinguaQuestDataError> =
        safeApiCall { api.loginWithGoogle(body).data }

    override suspend fun sendRegistrationOtp(body: EmailRequestDto): LinguaQuestResult<Unit, LinguaQuestDataError> =
        safeApiCall { api.sendRegistrationOtp(body).data }

}