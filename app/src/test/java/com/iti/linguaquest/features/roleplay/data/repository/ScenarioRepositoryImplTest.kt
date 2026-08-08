package com.iti.linguaquest.features.roleplay.data.repository

import com.iti.linguaquest.features.roleplay.data.datasource.local.ScenarioLocalDataSource
import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ScenarioRepositoryImplTest {

    private lateinit var localDataSource: ScenarioLocalDataSource
    private lateinit var repository: ScenarioRepositoryImpl

    @Before
    fun setUp() {
        localDataSource = mockk()
        repository = ScenarioRepositoryImpl(localDataSource)
    }

    @Test
    fun getBossScenarios_deserializesJsonIntoDomainModelsCorrectly() = runTest {
        // Given
        val json = """
            [
              {
                "id": "scenario_cafe_01",
                "worldId": "world_1",
                "bossName": "Pierre",
                "roleDescription": "Barista",
                "objective": "Order coffee",
                "voiceName": "Puck"
              }
            ]
        """.trimIndent()
        coEvery { localDataSource.getScenariosJson("fr") } returns json

        // When
        val scenarios = repository.getBossScenarios("fr")

        // Then
        assertEquals(1, scenarios.size)
        val scenario = scenarios.first()
        assertEquals(ScenarioId.SCENARIO_CAFE_01, scenario.id)
        assertEquals("world_1", scenario.worldId)
        assertEquals("Pierre", scenario.bossName)
        assertEquals("Barista", scenario.roleDescription)
        assertEquals("Order coffee", scenario.objective)
        assertEquals("Puck", scenario.voiceName)
        coVerify(exactly = 1) { localDataSource.getScenariosJson("fr") }
    }
}
