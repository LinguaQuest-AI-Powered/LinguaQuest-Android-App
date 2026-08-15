package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class VerifyEmailOtpUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private lateinit var useCase: VerifyEmailOtpUseCase

    @Before
    fun setUp() {
        useCase = VerifyEmailOtpUseCase(authRepository)
    }

    @Test
    fun invoke_returnsSuccess_whenRepositoryVerificationSucceeds() = runTest {
        // Given
        val email = "test@example.com"
        val code = "1234"
        coEvery { authRepository.verifyEmailOtp(email, code) } returns LinguaQuestResult.Success(true)

        // When
        val result = useCase(email, code)

        // Then
        assertEquals(LinguaQuestResult.Success(true), result)
        coVerify(exactly = 1) { authRepository.verifyEmailOtp(email, code) }
    }

    @Test
    fun invoke_returnsFailure_whenRepositoryVerificationFails() = runTest {
        // Given
        val email = "test@example.com"
        val code = "1234"
        val error = AuthError.InvalidOtp
        coEvery { authRepository.verifyEmailOtp(email, code) } returns LinguaQuestResult.Failure(error)

        // When
        val result = useCase(email, code)

        // Then
        assertEquals(LinguaQuestResult.Failure(error), result)
        coVerify(exactly = 1) { authRepository.verifyEmailOtp(email, code) }
    }
}
