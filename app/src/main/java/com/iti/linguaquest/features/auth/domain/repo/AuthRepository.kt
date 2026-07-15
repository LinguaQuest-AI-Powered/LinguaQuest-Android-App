package com.iti.linguaquest.features.auth.domain.repo

import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.core.network.EmptyResult
import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.features.auth.domain.model.AuthUserModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun register(
        email: String,
        username: String,
        password: String,
        nativeLanguage: String,
        targetLanguage: String
    ): LinguaQuestResult<AuthUserModel, LinguaQuestDataError>

    suspend fun login(email: String, password: String): LinguaQuestResult<AuthUserModel, LinguaQuestDataError>

    suspend fun signInWithGoogle(idToken: String): LinguaQuestResult<AuthUserModel, LinguaQuestDataError>

    suspend fun sendOtp(email: String, purpose: String): EmptyResult<LinguaQuestDataError>
    suspend fun verifyEmailOtp(email: String, otpCode: String): EmptyResult<LinguaQuestDataError>
    suspend fun verifyPasswordResetOtp(email: String, otpCode: String): LinguaQuestResult<String, LinguaQuestDataError>
    suspend fun setNewPassword(newPassword: String, resetToken: String): EmptyResult<LinguaQuestDataError>
    fun isLoggedIn(): Flow<Boolean>
    suspend fun logout(): EmptyResult<LinguaQuestDataError>
    suspend fun refreshToken(): EmptyResult<LinguaQuestDataError>
    suspend fun getAccessToken(): String?
}