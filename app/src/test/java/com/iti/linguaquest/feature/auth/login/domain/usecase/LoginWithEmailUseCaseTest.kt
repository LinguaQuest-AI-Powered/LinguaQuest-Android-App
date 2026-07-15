package com.iti.linguaquest.feature.auth.login.domain.usecase

import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.feature.auth.login.domain.model.AuthLoginResult
import com.iti.linguaquest.feature.auth.login.domain.model.AuthUser
import com.iti.linguaquest.feature.auth.login.domain.repository.LoginRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginWithEmailUseCaseTest {

    @Test
    fun invoke_passesEmailAndPasswordToRepository() = runBlocking {
        val repository = FakeLoginRepository()
        val useCase = LoginWithEmailUseCase(repository)

        useCase("a@b.com", "secret")

        assertEquals("a@b.com", repository.lastEmail)
        assertEquals("secret", repository.lastPassword)
    }

    private class FakeLoginRepository : LoginRepository {
        var lastEmail: String? = null
        var lastPassword: String? = null

        override suspend fun loginWithEmail(
            email: String,
            password: String,
        ): LinguaQuestResult<AuthLoginResult, LinguaQuestDataError.Auth> {
            lastEmail = email
            lastPassword = password
            return sampleResult()
        }

        override suspend fun loginWithGoogle(
            idToken: String,
        ): LinguaQuestResult<AuthLoginResult, LinguaQuestDataError.Auth> {
            return sampleResult()
        }

        override suspend fun continueAsGuest(): LinguaQuestResult<AuthUser, LinguaQuestDataError.Auth> {
            return LinguaQuestResult.Success(
                AuthUser(
                    id = 1,
                    username = "guest",
                    name = "Guest",
                    nativeLanguage = "Arabic",
                    isVerified = false,
                )
            )
        }

        private fun sampleResult(): LinguaQuestResult<AuthLoginResult, LinguaQuestDataError.Auth> {
            return LinguaQuestResult.Success(
                AuthLoginResult(
                    accessToken = "access",
                    refreshToken = "refresh",
                    tokenType = "Bearer",
                    expiresIn = 3600,
                    user = AuthUser(
                        id = 1,
                        username = "user",
                        name = "User",
                        nativeLanguage = "Arabic",
                        isVerified = true,
                    ),
                )
            )
        }
    }
}
