package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAiNextTurn
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameHistory
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameState
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderHistoryEntry
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderNextTurn
import com.iti.linguaquest.features.mindreader.domain.repository.MindReaderRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetMindReaderNextTurnUseCaseTest {

    private lateinit var repository: MindReaderRepository
    private lateinit var useCase: GetMindReaderNextTurnUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetMindReaderNextTurnUseCase(repository)
    }

    @Test
    fun invoke_returnsQuestion_whenResponseIsQuestion() = runTest {
        // Given
        val categoryContext = "Animals"
        val targetLanguage = "en"
        val nativeLanguage = "ar"
        val state = MindReaderGameState(
            history = MindReaderGameHistory(
                turns = listOf(
                    MindReaderHistoryEntry(
                        attributeId = "1",
                        questionTargetText = "Is it big?",
                        questionNativeText = "هل هو كبير؟",
                        answer = MindReaderAnswerOption.YES,
                        confidenceAfterAnswer = 0.5
                    )
                )
            )
        )
        val expectedAiResponse = MindReaderAiNextTurn.Question(
            targetText = "Does it have fur?",
            nativeText = "هل لديه فرو؟"
        )
        
        coEvery { 
            repository.getNextTurn(any(), any(), any(), any())
        } returns expectedAiResponse

        // When
        val result = useCase(categoryContext, targetLanguage, nativeLanguage, state)

        // Then
        assertTrue(result is MindReaderNextTurn.Question)
        val questionResult = result as MindReaderNextTurn.Question
        assertEquals("Does it have fur?", questionResult.question.targetText)
        assertEquals("هل لديه فرو؟", questionResult.question.nativeText)
        
        coVerify(exactly = 1) { 
            repository.getNextTurn(any(), any(), any(), any()) 
        }
    }

    @Test
    fun invoke_returnsGuess_whenResponseIsGuess() = runTest {
        // Given
        val categoryContext = "Animals"
        val targetLanguage = "en"
        val nativeLanguage = "ar"
        val state = MindReaderGameState()
        
        val expectedAiResponse = MindReaderAiNextTurn.Guess(
            word = "Cat",
            translation = "قطة",
            emoji = "🐱",
            quizChoices = emptyList()
        )
        
        coEvery { 
            repository.getNextTurn(any(), any(), any(), any())
        } returns expectedAiResponse

        // When
        val result = useCase(categoryContext, targetLanguage, nativeLanguage, state)

        // Then
        assertTrue(result is MindReaderNextTurn.Guess)
        val guessResult = result as MindReaderNextTurn.Guess
        assertEquals("Cat", guessResult.guess.entity.targetText)
        assertEquals("قطة", guessResult.guess.entity.nativeText)
        assertEquals("🐱", guessResult.guess.entity.emoji)
    }

    @Test(expected = Exception::class)
    fun invoke_throwsException_whenRepositoryThrowsException() = runTest {
        // Given
        coEvery { 
            repository.getNextTurn(any(), any(), any(), any())
        } throws Exception("Network error")

        // When
        useCase("test", "en", "ar", MindReaderGameState())
    }
}
