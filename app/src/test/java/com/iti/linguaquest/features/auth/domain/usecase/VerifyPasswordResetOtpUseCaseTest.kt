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

class VerifyPasswordResetOtpUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private lateinit var useCase: VerifyPasswordResetOtpUseCase

    @Before
    fun setUp() {
        useCase = VerifyPasswordResetOtpUseCase(authRepository)
    }

    @Test
    fun invoke_returnsSuccess_whenRepositoryVerificationSucceeds() = runTest {
        // Given
        val email = "test@example.com"
        val code = "1234"
        val resetToken = "reset_token"
        coEvery { authRepository.verifyPasswordResetOtp(email, code) } returns LinguaQuestResult.Success(resetToken)

        // When
        val result = useCase(email, code)

        // Then
        assertEquals(LinguaQuestResult.Success(resetToken), result)
        coVerify(exactly = 1) { authRepository.verifyPasswordResetOtp(email, code) }
    }

    @Test
    fun invoke_returnsFailure_whenRepositoryVerificationFails() = runTest {
        // Given
        val email = "test@example.com"
        val code = "1234"
        val error = AuthError.InvalidOtp
        coEvery { authRepository.verifyPasswordResetOtp(email, code) } returns LinguaQuestResult.Failure(error)

        // When
        val result = useCase(email, code)

        // Then
        assertEquals(LinguaQuestResult.Failure(error), result)
        coVerify(exactly = 1) { authRepository.verifyPasswordResetOtp(email, code) }
    }
}
