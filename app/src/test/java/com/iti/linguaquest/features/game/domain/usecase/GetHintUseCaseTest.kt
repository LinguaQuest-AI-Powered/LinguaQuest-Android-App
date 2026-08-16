package com.iti.linguaquest.features.game.domain.usecase


import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.game.domain.model.Hint
import com.iti.linguaquest.features.game.domain.repository.LevelRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetHintUseCaseTest {

    private val repository: LevelRepository = mockk()
    private lateinit var useCase: GetHintUseCase

    @Before
    fun setUp() {
        useCase = GetHintUseCase(repository)
    }

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        // Given
        val worldId = 1
        val levelId = 1
        val expectedHint = Hint("A red fruit", 10, 90)
        coEvery { repository.getHint(worldId, levelId) } returns LinguaQuestResult.Success(expectedHint)

        // When
        val result = useCase(worldId, levelId)

        // Then
        assertEquals(LinguaQuestResult.Success(expectedHint), result)
        coVerify(exactly = 1) { repository.getHint(worldId, levelId) }
    }

    @Test
    fun invoke_returnsFailure_whenRepositoryFails() = runTest {
        // Given
        val worldId = 1
        val levelId = 1
        val error = LinguaQuestDataError.Remote.SERVER
        coEvery { repository.getHint(worldId, levelId) } returns LinguaQuestResult.Failure(error)

        // When
        val result = useCase(worldId, levelId)

        // Then
        assertEquals(LinguaQuestResult.Failure(error), result)
        coVerify(exactly = 1) { repository.getHint(worldId, levelId) }
    }
}
