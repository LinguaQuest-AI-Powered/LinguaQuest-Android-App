package com.iti.linguaquest.features.gallery.domain.usecase

import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.gallery.domain.repository.WordRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteWordUseCaseTest {

    private lateinit var wordRepository: WordRepository
    private lateinit var deleteWordUseCase: DeleteWordUseCase

    @Before
    fun setup() {
        wordRepository = mockk()
        deleteWordUseCase = DeleteWordUseCase(wordRepository)
    }

    @Test
    fun invoke_returnsSuccess_whenRepositoryDeletesSuccessfully() = runTest {
        // Given
        val wordId = 1
        coEvery { wordRepository.deleteWordById(wordId) } returns LinguaQuestResult.Success(Unit)

        // When
        val result = deleteWordUseCase(wordId)

        // Then
        coVerify(exactly = 1) { wordRepository.deleteWordById(wordId) }
        assertTrue(result is LinguaQuestResult.Success)
    }

    @Test
    fun invoke_returnsFailure_whenRepositoryFails() = runTest {
        // Given
        val wordId = 1
        val error = LinguaQuestDataError.Local.DISK_FULL
        coEvery { wordRepository.deleteWordById(wordId) } returns LinguaQuestResult.Failure(error)

        // When
        val result = deleteWordUseCase(wordId)

        // Then
        coVerify(exactly = 1) { wordRepository.deleteWordById(wordId) }
        assertTrue(result is LinguaQuestResult.Failure)
        assertEquals(error, (result as LinguaQuestResult.Failure).error)
    }
}
