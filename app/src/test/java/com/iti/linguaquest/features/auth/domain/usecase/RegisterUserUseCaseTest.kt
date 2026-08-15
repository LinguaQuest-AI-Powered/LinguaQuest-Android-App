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

class RegisterUserUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private lateinit var useCase: RegisterUserUseCase

    @Before
    fun setUp() {
        useCase = RegisterUserUseCase(authRepository)
    }

    @Test
    fun invoke_returnsSuccess_whenRepositoryRegistrationSucceeds() = runTest {
        // Given
        val email = "test@example.com"
        val username = "username"
        val password = "Password123"
        coEvery { authRepository.register(email, username, password) } returns LinguaQuestResult.Success(Unit)

        // When
        val result = useCase(email, username, password)

        // Then
        assertEquals(LinguaQuestResult.Success(Unit), result)
        coVerify(exactly = 1) { authRepository.register(email, username, password) }
    }

    @Test
    fun invoke_returnsFailure_whenRepositoryRegistrationFails() = runTest {
        // Given
        val email = "test@example.com"
        val username = "username"
        val password = "Password123"
        val error = AuthError.EmailAlreadyExists
        coEvery { authRepository.register(email, username, password) } returns LinguaQuestResult.Failure(error)

        // When
        val result = useCase(email, username, password)

        // Then
        assertEquals(LinguaQuestResult.Failure(error), result)
        coVerify(exactly = 1) { authRepository.register(email, username, password) }
    }
}
