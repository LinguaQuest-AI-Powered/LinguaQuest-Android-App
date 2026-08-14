package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CheckUserLoggedInUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private lateinit var useCase: CheckUserLoggedInUseCase

    @Before
    fun setUp() {
        useCase = CheckUserLoggedInUseCase(authRepository)
    }

    @Test
    fun invoke_returnsFlowFromRepository() = runTest {
        // Given
        val flow = flowOf(true)
        every { authRepository.isLoggedIn() } returns flow

        // When
        val result = useCase()

        // Then
        assertEquals(flow, result)
        verify(exactly = 1) { authRepository.isLoggedIn() }
    }
}
