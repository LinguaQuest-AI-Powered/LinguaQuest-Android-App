package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SetNewPasswordUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private lateinit var useCase: SetNewPasswordUseCase

    @Before
    fun setUp() {
        useCase = SetNewPasswordUseCase(authRepository)
    }

    @Test
    fun invoke_returnsSuccess_whenRepositorySetSucceeds() = runTest {
        // Given
        val password = "Password123"
        val resetToken = "reset_token"
        coEvery { authRepository.setNewPassword(password, resetToken) } returns LinguaQuestResult.Success(Unit)

        // When
        val result = useCase(password, resetToken)

        // Then
        assertEquals(LinguaQuestResult.Success(Unit), result)
        coVerify(exactly = 1) { authRepository.setNewPassword(password, resetToken) }
    }

    @Test
    fun invoke_returnsFailure_whenRepositorySetFails() = runTest {
        // Given
        val password = "Password123"
        val resetToken = "reset_token"
        val error = AuthError.ResetTokenExpired
        coEvery { authRepository.setNewPassword(password, resetToken) } returns LinguaQuestResult.Failure(error)

        // When
        val result = useCase(password, resetToken)

        // Then
        assertTrue(result is LinguaQuestResult.Failure)
        assertEquals(error, (result as LinguaQuestResult.Failure).error)
        coVerify(exactly = 1) { authRepository.setNewPassword(password, resetToken) }
    }
}
