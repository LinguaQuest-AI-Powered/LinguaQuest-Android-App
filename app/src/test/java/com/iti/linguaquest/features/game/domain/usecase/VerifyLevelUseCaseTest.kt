package com.iti.linguaquest.features.game.domain.usecase


import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.game.domain.model.VerifyLevelResult
import com.iti.linguaquest.features.game.domain.repository.LevelRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File

class VerifyLevelUseCaseTest {

    private val repository: LevelRepository = mockk()
    private lateinit var useCase: VerifyLevelUseCase

    @Before
    fun setUp() {
        useCase = VerifyLevelUseCase(repository)
    }

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        // Given
        val worldId = 1
        val levelId = 1
        val file = mockk<File>()
        val expectedResult = VerifyLevelResult(
            isMatch = true,
            xpEarned = 100,
            coinsEarned = 50,
            level = 2,
            levelProgressPercentage = 50
        )
        coEvery { repository.verifyLevel(worldId, levelId, file) } returns LinguaQuestResult.Success(expectedResult)

        // When
        val result = useCase(worldId, levelId, file)

        // Then
        assertEquals(LinguaQuestResult.Success(expectedResult), result)
        coVerify(exactly = 1) { repository.verifyLevel(worldId, levelId, file) }
    }

    @Test
    fun invoke_returnsFailure_whenRepositoryFails() = runTest {
        // Given
        val worldId = 1
        val levelId = 1
        val file = mockk<File>()
        val error = LinguaQuestDataError.Remote.SERVER
        coEvery { repository.verifyLevel(worldId, levelId, file) } returns LinguaQuestResult.Failure(error)

        // When
        val result = useCase(worldId, levelId, file)

        // Then
        assertEquals(LinguaQuestResult.Failure(error), result)
        coVerify(exactly = 1) { repository.verifyLevel(worldId, levelId, file) }
    }
}
