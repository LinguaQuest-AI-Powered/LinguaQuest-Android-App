package com.iti.linguaquest.feature.auth.login.domain.usecase

import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.feature.auth.login.domain.model.AuthLoginResult
import com.iti.linguaquest.feature.auth.login.domain.model.AuthUser
import com.iti.linguaquest.feature.auth.login.domain.repository.LoginRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ContinueAsGuestUseCaseTest {

    @Test
    fun invoke_returnsRepositoryResult() = runBlocking {
        val expectedUser = AuthUser(
            id = 99,
            username = "guest",
            name = "Guest User",
            nativeLanguage = "Arabic",
            isVerified = false,
        )
        val repository = FakeLoginRepository(expectedUser)
        val useCase = ContinueAsGuestUseCase(repository)

        val result = useCase()

        assertEquals(expectedUser, (result as LinguaQuestResult.Success).data)
    }

    private class FakeLoginRepository(
        private val user: AuthUser,
    ) : LoginRepository {
        override suspend fun loginWithEmail(
            email: String,
            password: String,
        ): LinguaQuestResult<AuthLoginResult, LinguaQuestDataError.Auth> {
            return sampleResult()
        }

        override suspend fun loginWithGoogle(
            idToken: String,
        ): LinguaQuestResult<AuthLoginResult, LinguaQuestDataError.Auth> {
            return sampleResult()
        }

        override suspend fun continueAsGuest(): LinguaQuestResult<AuthUser, LinguaQuestDataError.Auth> {
            return LinguaQuestResult.Success(user)
        }

        private fun sampleResult(): LinguaQuestResult<AuthLoginResult, LinguaQuestDataError.Auth> {
            return LinguaQuestResult.Success(
                AuthLoginResult(
                    accessToken = "access",
                    refreshToken = "refresh",
                    tokenType = "Bearer",
                    expiresIn = 3600,
                    user = user,
                )
            )
        }
    }
}
