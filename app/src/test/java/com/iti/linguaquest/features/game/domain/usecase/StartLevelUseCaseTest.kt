package com.iti.linguaquest.features.game.domain.usecase


import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.game.domain.repository.LevelRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StartLevelUseCaseTest {

    private val repository: LevelRepository = mockk()
    private lateinit var useCase: StartLevelUseCase

    @Before
    fun setUp() {
        useCase = StartLevelUseCase(repository)
    }

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        // Given
        val worldId = 1
        val levelId = 1
        val expectedWord = "Apple"
        coEvery { repository.startLevel(worldId, levelId) } returns LinguaQuestResult.Success(expectedWord)

        // When
        val result = useCase(worldId, levelId)

        // Then
        assertEquals(LinguaQuestResult.Success(expectedWord), result)
        coVerify(exactly = 1) { repository.startLevel(worldId, levelId) }
    }

    @Test
    fun invoke_returnsFailure_whenRepositoryFails() = runTest {
        // Given
        val worldId = 1
        val levelId = 1
        val error = LinguaQuestDataError.Remote.NO_INTERNET
        coEvery { repository.startLevel(worldId, levelId) } returns LinguaQuestResult.Failure(error)

        // When
        val result = useCase(worldId, levelId)

        // Then
        assertEquals(LinguaQuestResult.Failure(error), result)
        coVerify(exactly = 1) { repository.startLevel(worldId, levelId) }
    }
}
