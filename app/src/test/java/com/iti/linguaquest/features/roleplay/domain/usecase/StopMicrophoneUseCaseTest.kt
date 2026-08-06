package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class StopMicrophoneUseCaseTest {

    private lateinit var repository: RoleplayRepository
    private lateinit var useCase: StopMicrophoneUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = StopMicrophoneUseCase(repository)
    }

    @Test
    fun invoke_delegatesToRepositoryStopMicrophone() {
        // When
        useCase()

        // Then
        verify(exactly = 1) { repository.stopMicrophone() }
    }
}
