package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DisconnectRoleplayUseCaseTest {

    private lateinit var repository: RoleplayRepository
    private lateinit var useCase: DisconnectRoleplayUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = DisconnectRoleplayUseCase(repository)
    }

    @Test
    fun invoke_delegatesToRepositoryDisconnect() = runTest {
        // When
        useCase()

        // Then
        coVerify(exactly = 1) { repository.disconnect() }
    }
}
