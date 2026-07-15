package com.iti.linguaquest.feature.auth.login.domain.usecase

import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.feature.auth.login.domain.model.AuthLoginResult
import com.iti.linguaquest.feature.auth.login.domain.model.AuthUser
import com.iti.linguaquest.feature.auth.login.domain.repository.LoginRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginWithGoogleUseCaseTest {

    @Test
    fun invoke_passesIdTokenToRepository() = runBlocking {
        val repository = FakeLoginRepository()
        val useCase = LoginWithGoogleUseCase(repository)

        useCase("google-token")

        assertEquals("google-token", repository.lastIdToken)
    }

    private class FakeLoginRepository : LoginRepository {
        var lastIdToken: String? = null

        override suspend fun loginWithEmail(
            email: String,
            password: String,
        ): LinguaQuestResult<AuthLoginResult, LinguaQuestDataError.Auth> {
            return sampleResult()
        }

        override suspend fun loginWithGoogle(
            idToken: String,
        ): LinguaQuestResult<AuthLoginResult, LinguaQuestDataError.Auth> {
            lastIdToken = idToken
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
