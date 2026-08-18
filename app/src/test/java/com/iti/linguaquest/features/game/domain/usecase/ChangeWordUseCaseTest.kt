package com.iti.linguaquest.features.game.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.game.domain.repository.LevelRepository
import com.iti.linguaquest.core.session.SessionEventBus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ChangeWordUseCaseTest {

    private val repository: LevelRepository = mockk()
    private val sessionEventBus: SessionEventBus = mockk(relaxed = true)
    private lateinit var useCase: ChangeWordUseCase

    @Before
    fun setUp() {
        useCase = ChangeWordUseCase(repository, sessionEventBus)
    }

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        // Given
        val worldId = 1
        val levelId = 1
        val expectedWord = "Banana"
        coEvery { repository.changeWord(worldId, levelId) } returns LinguaQuestResult.Success(expectedWord)

        // When
        val result = useCase(worldId, levelId)

        // Then
        assertEquals(LinguaQuestResult.Success(expectedWord), result)
        coVerify(exactly = 1) { repository.changeWord(worldId, levelId) }
    }

    @Test
    fun invoke_returnsFailure_whenRepositoryFails() = runTest {
        // Given
        val worldId = 1
        val levelId = 1
        val error = LinguaQuestDataError.Remote.REQUEST_TIMEOUT
        coEvery { repository.changeWord(worldId, levelId) } returns LinguaQuestResult.Failure(error)

        // When
        val result = useCase(worldId, levelId)

        // Then
        assertEquals(LinguaQuestResult.Failure(error), result)
        coVerify(exactly = 1) { repository.changeWord(worldId, levelId) }
    }
}
