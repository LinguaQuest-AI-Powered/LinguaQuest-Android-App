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

class SendRegistrationOtpUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private lateinit var useCase: SendRegistrationOtpUseCase

    @Before
    fun setUp() {
        useCase = SendRegistrationOtpUseCase(authRepository)
    }

    @Test
    fun invoke_returnsSuccess_whenRepositorySendsOtpSucceeds() = runTest {
        // Given
        val email = "test@example.com"
        coEvery { authRepository.sendRegistrationOtp(email) } returns LinguaQuestResult.Success(Unit)

        // When
        val result = useCase(email)

        // Then
        assertEquals(LinguaQuestResult.Success(Unit), result)
        coVerify(exactly = 1) { authRepository.sendRegistrationOtp(email) }
    }

    @Test
    fun invoke_returnsFailure_whenRepositorySendsOtpFails() = runTest {
        // Given
        val email = "test@example.com"
        val error = AuthError.EmailAlreadyExists
        coEvery { authRepository.sendRegistrationOtp(email) } returns LinguaQuestResult.Failure(error)

        // When
        val result = useCase(email)

        // Then
        assertEquals(LinguaQuestResult.Failure(error), result)
        coVerify(exactly = 1) { authRepository.sendRegistrationOtp(email) }
    }
}
