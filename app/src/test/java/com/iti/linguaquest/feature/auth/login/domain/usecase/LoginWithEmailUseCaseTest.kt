package com.iti.linguaquest.feature.auth.login.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import com.iti.linguaquest.features.auth.domain.usecase.LoginUserUseCase
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginWithEmailUseCaseTest {

    @Test
    fun invoke_passesEmailAndPasswordToRepository() = runBlocking {
        val repository = FakeAuthRepository()
        val useCase = LoginUserUseCase(repository)

        useCase("a@b.com", "secret")

        assertEquals("a@b.com", repository.lastEmail)
        assertEquals("secret", repository.lastPassword)
    }

    private class FakeAuthRepository : AuthRepository {
        var lastEmail: String? = null
        var lastPassword: String? = null

        override suspend fun getAuthLanguages(): LinguaQuestResult<List<LanguageOption>, AuthError> {
            return LinguaQuestResult.Success(emptyList())
        }

        override suspend fun register(
            email: String,
            username: String,
            password: String
        ): LinguaQuestResult<Unit, AuthError> {
            return LinguaQuestResult.Success(Unit)
        }

        override suspend fun login(
            email: String,
            password: String
        ): LinguaQuestResult<Unit, AuthError> {
            lastEmail = email
            lastPassword = password
            return LinguaQuestResult.Success(Unit)
        }

        override suspend fun signInWithGoogle(idToken: String): LinguaQuestResult<Boolean, AuthError> {
            return LinguaQuestResult.Success(true)
        }

        override suspend fun completeOAuthProfile(
            nativeLanguageId: Int,
            targetLanguageId: Int,
            username: String?
        ): LinguaQuestResult<Unit, AuthError> {
            return LinguaQuestResult.Success(Unit)
        }

        override suspend fun sendRegistrationOtp(email: String): LinguaQuestResult<Unit, AuthError> {
            return LinguaQuestResult.Success(Unit)
        }

        override suspend fun sendPasswordResetOtp(email: String): LinguaQuestResult<Unit, AuthError> {
            return LinguaQuestResult.Success(Unit)
        }

        override suspend fun verifyEmailOtp(email: String, otpCode: String): LinguaQuestResult<Boolean, AuthError> {
            return LinguaQuestResult.Success(true)
        }

        override suspend fun verifyPasswordResetOtp(email: String, otpCode: String): LinguaQuestResult<String, AuthError> {
            return LinguaQuestResult.Success("token")
        }

        override suspend fun setNewPassword(newPassword: String, resetToken: String): LinguaQuestResult<Unit, AuthError> {
            return LinguaQuestResult.Success(Unit)
        }

        override fun isLoggedIn(): Flow<Boolean> = flowOf(true)

        override suspend fun logout(): LinguaQuestResult<Unit, AuthError> {
            return LinguaQuestResult.Success(Unit)
        }

        override suspend fun refreshToken(refreshToken: String): LinguaQuestResult<Unit, AuthError> {
            return LinguaQuestResult.Success(Unit)
        }
    }
}
