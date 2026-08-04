package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SignInWithGoogleUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private val useCase = SignInWithGoogleUseCase(authRepository)

    @Test
    fun invokeReturnsSuccessWhenRepositorySignInSucceeds() = runTest {
        val idToken = "google-id-token"
        coEvery { authRepository.signInWithGoogle(idToken) } returns LinguaQuestResult.Success(true)

        val result = useCase(idToken)

        assertEquals(LinguaQuestResult.Success(true), result)
        coVerify(exactly = 1) { authRepository.signInWithGoogle(idToken) }
    }

    @Test
    fun invokeReturnsErrorWhenRepositorySignInFails() = runTest {
        val idToken = "invalid-token"
        val error: AuthError = AuthError.InvalidIdToken
        coEvery { authRepository.signInWithGoogle(idToken) } returns LinguaQuestResult.Failure(error)

        val result = useCase(idToken)

        assertEquals(LinguaQuestResult.Failure(error), result)
        coVerify(exactly = 1) { authRepository.signInWithGoogle(idToken) }
    }
}
