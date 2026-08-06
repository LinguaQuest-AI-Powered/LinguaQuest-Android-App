package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ConnectToBossStageUseCaseTest {

    private lateinit var repository: RoleplayRepository
    private lateinit var useCase: ConnectToBossStageUseCase

    private val sampleScenario = BossScenario(
        id = ScenarioId.SCENARIO_CAFE_01,
        worldId = "world_1",
        bossName = "Pierre",
        roleDescription = "Barista",
        objective = "Order coffee",
        voiceName = "Puck"
    )

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = ConnectToBossStageUseCase(repository)
    }

    @Test
    fun invoke_delegatesToRepositoryConnectToBossStage() = runTest {
        // When
        useCase(sampleScenario)

        // Then
        coVerify(exactly = 1) { repository.connectToBossStage(sampleScenario) }
    }
}
