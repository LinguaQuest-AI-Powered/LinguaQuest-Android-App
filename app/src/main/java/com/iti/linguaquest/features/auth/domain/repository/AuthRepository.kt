package com.iti.linguaquest.features.auth.domain.repository

import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun register(
        email: String,
        username: String,
        password: String,
    ): LinguaQuestResult<Unit, AuthError>

    suspend fun login(
        email: String,
        password: String
    ): LinguaQuestResult<Unit, AuthError>

    suspend fun signInWithGoogle(
        idToken: String
    ): LinguaQuestResult<Unit, AuthError>

    suspend fun sendRegistrationOtp(email: String): LinguaQuestResult<Unit, AuthError>
    suspend fun sendPasswordResetOtp(email: String): LinguaQuestResult<Unit, AuthError>

    suspend fun verifyEmailOtp(email: String, otpCode: String): LinguaQuestResult<Unit, AuthError>
    suspend fun verifyPasswordResetOtp(email: String, otpCode: String): LinguaQuestResult<String, AuthError>
    suspend fun setNewPassword(newPassword: String, resetToken: String): LinguaQuestResult<Unit, AuthError>
    fun isLoggedIn(): Flow<Boolean>
    suspend fun logout(): LinguaQuestResult<Unit, AuthError>
}