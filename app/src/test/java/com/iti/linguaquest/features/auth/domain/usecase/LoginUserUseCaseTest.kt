package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import com.iti.linguaquest.features.auth.domain.model.AuthUser
import com.iti.linguaquest.features.notification.domain.usecase.RegisterDeviceTokenUseCase
import com.iti.linguaquest.features.auth.domain.usecase.SyncUserNativeLanguageUseCase
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
    private lateinit var syncUserNativeLanguageUseCase: SyncUserNativeLanguageUseCase
    private lateinit var useCase: LoginUserUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        registerDeviceTokenUseCase = mockk(relaxed = true)
        syncUserNativeLanguageUseCase = mockk(relaxed = true)
        useCase = LoginUserUseCase(authRepository, registerDeviceTokenUseCase, syncUserNativeLanguageUseCase)
    }

    @Test
    fun invoke_returnsSuccess_whenRepositoryLoginSucceeds() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val authUser = AuthUser(
            id = 1,
            username = "Test",
            photo = null,
            nativeLanguage = null,
            isVerified = false,
            targetLanguages = emptyList()
        )
        coEvery { authRepository.login(email, password) } returns LinguaQuestResult.Success(authUser)

        // When
        val result = useCase(email, password)

        // Then
        assertEquals(LinguaQuestResult.Success(Unit), result)
        coVerify(exactly = 1) { authRepository.login(email, password) }
    }

    @Test
    fun invoke_returnsError_whenRepositoryLoginFails() = runTest {
        // Given
        val email = "test@example.com"
        val password = "wrongpassword"
        val error: AuthError = AuthError.InvalidCredentials
        coEvery { authRepository.login(email, password) } returns LinguaQuestResult.Failure(error)

        // When
        val result = useCase(email, password)

        // Then
        assertEquals(LinguaQuestResult.Failure(error), result)
        coVerify(exactly = 1) { authRepository.login(email, password) }
    }
}

