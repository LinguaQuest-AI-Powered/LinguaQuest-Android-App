package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import com.iti.linguaquest.features.notification.domain.usecase.RegisterDeviceTokenUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CompleteOAuthProfileUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private val registerDeviceTokenUseCase: RegisterDeviceTokenUseCase = mockk(relaxed = true)
    private val syncUserNativeLanguageUseCase: SyncUserNativeLanguageUseCase = mockk(relaxed = true)
    private lateinit var useCase: CompleteOAuthProfileUseCase

    @Before
    fun setUp() {
        useCase = CompleteOAuthProfileUseCase(
            authRepository = authRepository,
            registerDeviceTokenUseCase = registerDeviceTokenUseCase,
            syncUserNativeLanguageUseCase = syncUserNativeLanguageUseCase
        )
    }

    @Test
    fun invoke_returnsSuccessAndSyncsLanguages_whenRepositoryCallSucceeds() = runTest {
        // Given
        coEvery { authRepository.completeOAuthProfile(1, 2, "test_user") } returns LinguaQuestResult.Success(Unit)

        // When
        val result = useCase(1, 2, "test_user")

        // Then
        assertEquals(LinguaQuestResult.Success(Unit), result)
        coVerify(exactly = 1) { authRepository.completeOAuthProfile(1, 2, "test_user") }
        coVerify(exactly = 1) { syncUserNativeLanguageUseCase(nativeLanguageId = 1) }
        coVerify(exactly = 1) { registerDeviceTokenUseCase() }
    }

    @Test
    fun invoke_returnsFailure_whenRepositoryCallFails() = runTest {
        // Given
        val error = AuthError.InternalServerError
        coEvery { authRepository.completeOAuthProfile(1, 2, "test_user") } returns LinguaQuestResult.Failure(error)

        // When
        val result = useCase(1, 2, "test_user")

        // Then
        assertEquals(LinguaQuestResult.Failure(error), result)
        coVerify(exactly = 1) { authRepository.completeOAuthProfile(1, 2, "test_user") }
        coVerify(exactly = 0) { syncUserNativeLanguageUseCase(any()) }
        coVerify(exactly = 0) { registerDeviceTokenUseCase() }
    }
}
