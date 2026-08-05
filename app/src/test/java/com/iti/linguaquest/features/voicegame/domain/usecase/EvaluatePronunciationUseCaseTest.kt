package com.iti.linguaquest.features.voicegame.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.voicegame.domain.model.VoiceEvaluation
import com.iti.linguaquest.features.voicegame.domain.repository.VoiceGameRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EvaluatePronunciationUseCaseTest {

    private lateinit var repository: VoiceGameRepository
    private lateinit var useCase: EvaluatePronunciationUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = EvaluatePronunciationUseCase(repository)
    }

    @Test
    fun invoke_returnsSuccessResult_whenRepositorySucceeds() = runTest {
        val audioData = byteArrayOf(1, 2, 3)
        val expectedEvaluation = VoiceEvaluation(
            rating = 9,
            correctWords = listOf("hello", "world"),
            wrongWords = emptyList(),
            advice = "Great job!"
        )
        coEvery {
            repository.evaluatePronunciation("Hello world", "English", audioData, "Spanish")
        } returns LinguaQuestResult.Success(expectedEvaluation)

        val result = useCase("Hello world", "English", audioData, "Spanish")

        assertTrue(result is LinguaQuestResult.Success)
        assertEquals(expectedEvaluation, (result as LinguaQuestResult.Success).data)
        coVerify(exactly = 1) {
            repository.evaluatePronunciation("Hello world", "English", audioData, "Spanish")
        }
    }

    @Test
    fun invoke_returnsFailureResult_whenRepositoryFails() = runTest {
        val audioData = byteArrayOf(4, 5, 6)
        val expectedError = LinguaQuestDataError.CustomServerMessage("Audio processing failed")
        coEvery {
            repository.evaluatePronunciation("Goodbye", "French", audioData, null)
        } returns LinguaQuestResult.Failure(expectedError)

        val result = useCase("Goodbye", "French", audioData)

        assertTrue(result is LinguaQuestResult.Failure)
        assertEquals(expectedError, (result as LinguaQuestResult.Failure).error)
        coVerify(exactly = 1) {
            repository.evaluatePronunciation("Goodbye", "French", audioData, null)
        }
    }
}
