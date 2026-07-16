package com.iti.linguaquest.features.auth.data.datasource

import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.core.network.LinguaQuestResult

interface AuthRemoteDataSource {
    suspend fun register(body: RegisterRequestDto): LinguaQuestResult<RegisterResponseDataDto, LinguaQuestDataError>
    suspend fun login(body: LoginRequestDto): LinguaQuestResult<LoginResponseDataDto, LinguaQuestDataError>
    suspend fun loginWithGoogle(body: OAuthGoogleRequestDto): LinguaQuestResult<OAuthResponseDataDto, LinguaQuestDataError>
    suspend fun sendRegistrationOtp(body: EmailRequestDto): LinguaQuestResult<Unit, LinguaQuestDataError>
}