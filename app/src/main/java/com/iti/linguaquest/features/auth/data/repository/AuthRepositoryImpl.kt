package com.iti.linguaquest.features.auth.data.repository

import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.model.AuthUser
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow


class AuthRepositoryImpl @Inject constructor(

): AuthRepository{
    override suspend fun register(
        email: String,
        username: String,
        password: String,
        nativeLanguage: String,
        targetLanguage: String
    ): LinguaQuestResult<AuthUser, AuthError> {
        TODO("Not yet implemented")
    }

    override suspend fun login(
        email: String,
        password: String
    ): LinguaQuestResult<AuthUser, AuthError> {
        TODO("Not yet implemented")
    }

    override suspend fun signInWithGoogle(idToken: String): LinguaQuestResult<AuthUser, AuthError> {
        TODO("Not yet implemented")
    }

    override suspend fun sendRegistrationOtp(email: String): LinguaQuestResult<Unit, AuthError> {
        TODO("Not yet implemented")
    }

    override suspend fun sendPasswordResetOtp(email: String): LinguaQuestResult<Unit, AuthError> {
        TODO("Not yet implemented")
    }

    override suspend fun verifyEmailOtp(
        email: String,
        otpCode: String
    ): LinguaQuestResult<Unit, AuthError> {
        TODO("Not yet implemented")
    }

    override suspend fun verifyPasswordResetOtp(
        email: String,
        otpCode: String
    ): LinguaQuestResult<String, AuthError> {
        TODO("Not yet implemented")
    }

    override suspend fun setNewPassword(
        newPassword: String,
        resetToken: String
    ): LinguaQuestResult<Unit, AuthError> {
        TODO("Not yet implemented")
    }

    override fun isLoggedIn(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): LinguaQuestResult<Unit, AuthError> {
        TODO("Not yet implemented")
    }

}