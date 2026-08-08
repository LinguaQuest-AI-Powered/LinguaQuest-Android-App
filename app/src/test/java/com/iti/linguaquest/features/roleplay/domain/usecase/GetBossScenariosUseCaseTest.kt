package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId
import com.iti.linguaquest.features.roleplay.domain.repository.ScenarioRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetBossScenariosUseCaseTest {

    private lateinit var repository: ScenarioRepository
    private lateinit var useCase: GetBossScenariosUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetBossScenariosUseCase(repository)
    }

    @Test
    fun invoke_delegatesToRepositoryGetBossScenarios() = runTest {
        // Given
        val expectedScenarios = listOf(
            BossScenario(
                id = ScenarioId.SCENARIO_CAFE_01,
                worldId = "world_1",
                bossName = "Pierre",
                roleDescription = "Barista",
                objective = "Order coffee",
                voiceName = "Puck"
            )
        )
        coEvery { repository.getBossScenarios("en") } returns expectedScenarios

        // When
        val result = useCase("en")

        // Then
        assertEquals(expectedScenarios, result)
        coVerify(exactly = 1) { repository.getBossScenarios("en") }
    }
}
