package com.iti.linguaquest.feature.auth.login.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import com.iti.linguaquest.features.auth.domain.usecase.CheckUserLoggedInUseCase
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class ContinueAsGuestUseCaseTest {

    @Test
    fun invoke_returnsLoggedInStateFromRepository() = runBlocking {
        val repository = FakeAuthRepository(loggedIn = true)
        val useCase = CheckUserLoggedInUseCase(repository)

        val result = useCase().first()

        assertTrue(result)
    }

    private class FakeAuthRepository(
        private val loggedIn: Boolean,
    ) : AuthRepository {
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

        override fun isLoggedIn(): Flow<Boolean> = flowOf(loggedIn)

        override suspend fun logout(): LinguaQuestResult<Unit, AuthError> {
            return LinguaQuestResult.Success(Unit)
        }

        override suspend fun refreshToken(refreshToken: String): LinguaQuestResult<Unit, AuthError> {
            return LinguaQuestResult.Success(Unit)
        }
    }
}
