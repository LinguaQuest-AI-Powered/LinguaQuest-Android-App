package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import com.iti.linguaquest.features.notification.domain.usecase.UnregisterDeviceTokenUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LogoutUserUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private val unregisterDeviceTokenUseCase: UnregisterDeviceTokenUseCase = mockk(relaxed = true)
    private lateinit var useCase: LogoutUserUseCase

    @Before
    fun setUp() {
        useCase = LogoutUserUseCase(
            authRepository = authRepository,
            unregisterDeviceTokenUseCase = unregisterDeviceTokenUseCase
        )
    }

    @Test
    fun invoke_unregistersDeviceTokenAndLogsOut_whenSucceeds() = runTest {
        // Given
        coEvery { authRepository.logout() } returns LinguaQuestResult.Success(Unit)

        // When
        val result = useCase()

        // Then
        assertEquals(LinguaQuestResult.Success(Unit), result)
        coVerify(exactly = 1) { unregisterDeviceTokenUseCase() }
        coVerify(exactly = 1) { authRepository.logout() }
    }

    @Test
    fun invoke_stillLogsOut_whenDeviceTokenUnregistrationThrowsException() = runTest {
        // Given
        coEvery { unregisterDeviceTokenUseCase() } throws RuntimeException("Network Error")
        coEvery { authRepository.logout() } returns LinguaQuestResult.Success(Unit)

        // When
        val result = useCase()

        // Then
        assertEquals(LinguaQuestResult.Success(Unit), result)
        coVerify(exactly = 1) { authRepository.logout() }
    }

    @Test
    fun invoke_returnsFailure_whenRepositoryLogoutFails() = runTest {
        // Given
        val error = AuthError.InternalServerError
        coEvery { authRepository.logout() } returns LinguaQuestResult.Failure(error)

        // When
        val result = useCase()

        // Then
        assertEquals(LinguaQuestResult.Failure(error), result)
        coVerify(exactly = 1) { authRepository.logout() }
    }
}
