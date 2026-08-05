package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class StartMicrophoneUseCaseTest {

    private lateinit var repository: RoleplayRepository
    private lateinit var useCase: StartMicrophoneUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = StartMicrophoneUseCase(repository)
    }

    @Test
    fun invoke_delegatesToRepositoryStartMicrophone() {
        // When
        useCase()

        // Then
        verify(exactly = 1) { repository.startMicrophone() }
    }
}
