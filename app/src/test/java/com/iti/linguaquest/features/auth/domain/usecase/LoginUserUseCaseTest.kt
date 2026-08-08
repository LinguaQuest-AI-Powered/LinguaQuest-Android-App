package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.features.notification.domain.usecase.RegisterDeviceTokenUseCase
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

class LoginUserUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var registerDeviceTokenUseCase: RegisterDeviceTokenUseCase
    private lateinit var useCase: LoginUserUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        registerDeviceTokenUseCase = mockk(relaxed = true)
        useCase = LoginUserUseCase(authRepository, registerDeviceTokenUseCase)
    }

    @Test
    fun invokeReturnsSuccessWhenRepositoryLoginSucceeds() = runTest {
        val email = "test@example.com"
        val password = "password123"
        coEvery { authRepository.login(email, password) } returns LinguaQuestResult.Success(Unit)

        val result = useCase(email, password)

        assertEquals(LinguaQuestResult.Success(Unit), result)
        coVerify(exactly = 1) { authRepository.login(email, password) }
    }

    @Test
    fun invokeReturnsErrorWhenRepositoryLoginFails() = runTest {
        val email = "test@example.com"
        val password = "wrongpassword"
        val error: AuthError = AuthError.InvalidCredentials
        coEvery { authRepository.login(email, password) } returns LinguaQuestResult.Failure(error)

        val result = useCase(email, password)

        assertEquals(LinguaQuestResult.Failure(error), result)
        coVerify(exactly = 1) { authRepository.login(email, password) }
    }
}
