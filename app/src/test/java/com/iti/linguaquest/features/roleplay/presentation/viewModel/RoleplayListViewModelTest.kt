package com.iti.linguaquest.features.roleplay.presentation.viewModel

import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId
import com.iti.linguaquest.features.roleplay.domain.usecase.GetBossScenariosUseCase
import com.iti.linguaquest.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RoleplayListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getBossScenariosUseCase: GetBossScenariosUseCase
    private lateinit var viewModel: RoleplayListViewModel

    @Before
    fun setUp() {
        getBossScenariosUseCase = mockk()
    }

    @Test
    fun init_loadsScenariosSuccessfully_whenUseCaseReturnsList() = runTest {
        // Given
        val expectedScenarios = listOf(
            BossScenario(
                id = ScenarioId.SCENARIO_CAFE_01,
                worldId = "world_1",
                bossName = "Barista Pierre",
                roleDescription = "Busy Parisian barista",
                objective = "Order a croissant and cafe au lait",
                voiceName = "Puck"
            )
        )
        coEvery { getBossScenariosUseCase(any()) } returns expectedScenarios

        // When
        viewModel = RoleplayListViewModel(getBossScenariosUseCase)

        // Then
        assertEquals(expectedScenarios, viewModel.state.value.scenarios)
        assertFalse(viewModel.state.value.isLoading)
        coVerify(exactly = 1) { getBossScenariosUseCase(any()) }
    }

    @Test
    fun init_handlesErrorGracefully_whenUseCaseThrowsException() = runTest {
        // Given
        coEvery { getBossScenariosUseCase(any()) } throws RuntimeException("Local json error")

        // When
        viewModel = RoleplayListViewModel(getBossScenariosUseCase)

        // Then
        assertEquals(emptyList<BossScenario>(), viewModel.state.value.scenarios)
        assertFalse(viewModel.state.value.isLoading)
        coVerify(exactly = 1) { getBossScenariosUseCase(any()) }
    }
}
