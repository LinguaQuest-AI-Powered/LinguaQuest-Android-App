package com.iti.linguaquest.features.game.data.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.game.data.remote.LevelRemoteDataSource
import com.iti.linguaquest.features.game.data.remote.dto.HintDto
import com.iti.linguaquest.features.game.data.remote.dto.StartLevelDto
import com.iti.linguaquest.features.game.data.remote.dto.VerifyLevelDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

class LevelRepositoryImplTest {

    private val remoteDataSource: LevelRemoteDataSource = mockk()
    private lateinit var repository: LevelRepositoryImpl

    @Before
    fun setUp() {
        repository = LevelRepositoryImpl(remoteDataSource)
    }

    @Test
    fun startLevel_returnsSuccessWithWord_whenRemoteCallSucceeds() = runTest {
        // Given
        val worldId = 1
        val order = 1
        val expectedWord = "Apple"
        val response = StartLevelDto(targetWord = expectedWord)
        coEvery { remoteDataSource.startLevel(worldId, order) } returns LinguaQuestResult.Success(response)

        // When
        val result = repository.startLevel(worldId, order)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        assertEquals(expectedWord, (result as LinguaQuestResult.Success).data)
        coVerify(exactly = 1) { remoteDataSource.startLevel(worldId, order) }
    }

    @Test
    fun startLevel_returnsFailure_whenRemoteCallFails() = runTest {
        // Given
        val worldId = 1
        val order = 1
        val error = LinguaQuestDataError.Remote.NO_INTERNET
        coEvery { remoteDataSource.startLevel(worldId, order) } returns LinguaQuestResult.Failure(error)

        // When
        val result = repository.startLevel(worldId, order)

        // Then
        assertTrue(result is LinguaQuestResult.Failure)
        assertEquals(error, (result as LinguaQuestResult.Failure).error)
        coVerify(exactly = 1) { remoteDataSource.startLevel(worldId, order) }
    }

    @Test
    fun changeWord_returnsSuccessWithWord_whenRemoteCallSucceeds() = runTest {
        // Given
        val worldId = 1
        val order = 1
        val expectedWord = "Banana"
        val response = StartLevelDto(targetWord = expectedWord)
        coEvery { remoteDataSource.changeWord(worldId, order) } returns LinguaQuestResult.Success(response)

        // When
        val result = repository.changeWord(worldId, order)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        assertEquals(expectedWord, (result as LinguaQuestResult.Success).data)
        coVerify(exactly = 1) { remoteDataSource.changeWord(worldId, order) }
    }

    @Test
    fun changeWord_returnsFailure_whenRemoteCallFails() = runTest {
        // Given
        val worldId = 1
        val order = 1
        val error = LinguaQuestDataError.Remote.SERVER
        coEvery { remoteDataSource.changeWord(worldId, order) } returns LinguaQuestResult.Failure(error)

        // When
        val result = repository.changeWord(worldId, order)

        // Then
        assertTrue(result is LinguaQuestResult.Failure)
        assertEquals(error, (result as LinguaQuestResult.Failure).error)
        coVerify(exactly = 1) { remoteDataSource.changeWord(worldId, order) }
    }

    @Test
    fun verifyLevel_returnsMappedSuccess_whenRemoteCallSucceeds() = runTest {
        // Given
        val worldId = 1
        val order = 1
        val file = mockk<File>()
        val response = VerifyLevelDto(
            isMatch = true,
            xpEarned = 100,
            coinsEarned = 50,
            level = 2,
            levelProgressPercentage = 75
        )
        coEvery { remoteDataSource.verifyLevel(worldId, order, file) } returns LinguaQuestResult.Success(response)

        // When
        val result = repository.verifyLevel(worldId, order, file)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        val data = (result as LinguaQuestResult.Success).data
        assertEquals(true, data.isMatch)
        assertEquals(100, data.xpEarned)
        assertEquals(50, data.coinsEarned)
        assertEquals(2, data.level)
        assertEquals(75, data.levelProgressPercentage)
        coVerify(exactly = 1) { remoteDataSource.verifyLevel(worldId, order, file) }
    }

    @Test
    fun verifyLevel_returnsFailure_whenRemoteCallFails() = runTest {
        // Given
        val worldId = 1
        val order = 1
        val file = mockk<File>()
        val error = LinguaQuestDataError.Remote.BAD_REQUEST
        coEvery { remoteDataSource.verifyLevel(worldId, order, file) } returns LinguaQuestResult.Failure(error)

        // When
        val result = repository.verifyLevel(worldId, order, file)

        // Then
        assertTrue(result is LinguaQuestResult.Failure)
        assertEquals(error, (result as LinguaQuestResult.Failure).error)
        coVerify(exactly = 1) { remoteDataSource.verifyLevel(worldId, order, file) }
    }

    @Test
    fun getHint_returnsMappedSuccess_whenRemoteCallSucceeds() = runTest {
        // Given
        val worldId = 1
        val order = 1
        val response = HintDto(
            hint = "A red fruit",
            coinsSpent = 10,
            remainingCoins = 90
        )
        coEvery { remoteDataSource.getHint(worldId, order) } returns LinguaQuestResult.Success(response)

        // When
        val result = repository.getHint(worldId, order)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        val data = (result as LinguaQuestResult.Success).data
        assertEquals("A red fruit", data.hint)
        assertEquals(10, data.coinsSpent)
        assertEquals(90, data.remainingCoins)
        coVerify(exactly = 1) { remoteDataSource.getHint(worldId, order) }
    }

    @Test
    fun getHint_returnsFailure_whenRemoteCallFails() = runTest {
        // Given
        val worldId = 1
        val order = 1
        val error = LinguaQuestDataError.Remote.SERVER
        coEvery { remoteDataSource.getHint(worldId, order) } returns LinguaQuestResult.Failure(error)

        // When
        val result = repository.getHint(worldId, order)

        // Then
        assertTrue(result is LinguaQuestResult.Failure)
        assertEquals(error, (result as LinguaQuestResult.Failure).error)
        coVerify(exactly = 1) { remoteDataSource.getHint(worldId, order) }
    }
}
