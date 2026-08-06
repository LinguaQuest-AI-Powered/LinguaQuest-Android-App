package com.iti.linguaquest.features.setting.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ToggleNotificationsUseCaseTest {

    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var useCase: ToggleNotificationsUseCase

    @Before
    fun setUp() {
        // Given
        userPreferencesRepository = mockk(relaxed = true)
        useCase = ToggleNotificationsUseCase(userPreferencesRepository)
    }

    @Test
    fun invoke_savesNotificationsEnabledState_whenCalled() = runTest {
        // Given
        val enabled = false

        // When
        useCase(enabled)

        // Then
        coVerify { userPreferencesRepository.saveNotificationsEnabled(enabled) }
    }
}
