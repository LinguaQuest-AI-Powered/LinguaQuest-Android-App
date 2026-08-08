package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.LocalizedText
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAiNextTurn
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameHistory
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameState
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderHistoryEntry
import com.iti.linguaquest.features.mindreader.domain.repository.MindReaderRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
    fun `invoke with question response returns question entity`() = runTest {
        // Arrange
        val categoryContext = "Animals"
        val targetLanguage = "en"
        val nativeLanguage = "ar"
        val state = MindReaderGameState(
            history = MindReaderGameHistory(
                turns = listOf(
                    MindReaderHistoryEntry(
                        attributeId = "1",
                        question = LocalizedText(mapOf("en" to "Is it big?")),
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

        // Act
        val result = useCase(categoryContext, targetLanguage, nativeLanguage, state)

        // Assert
        assertTrue(result is MindReaderNextTurn.Question)
        val questionResult = result as MindReaderNextTurn.Question
        assertEquals("Does it have fur?", questionResult.question.question.resolve(targetLanguage))
        assertEquals("هل لديه فرو؟", questionResult.question.question.resolve(nativeLanguage))
        
        coVerify(exactly = 1) { 
            repository.getNextTurn(any(), any(), any(), any()) 
        }
    }

    @Test
    fun `invoke with guess response returns guess entity`() = runTest {
        // Arrange
        val categoryContext = "Animals"
        val targetLanguage = "en"
        val nativeLanguage = "ar"
        val state = MindReaderGameState()
        
        val expectedAiResponse = MindReaderAiNextTurn.Guess(
            word = "Cat",
            translation = "قطة",
            emoji = "🐱"
        )
        
        coEvery { 
            repository.getNextTurn(any(), any(), any(), any())
        } returns expectedAiResponse

        // Act
        val result = useCase(categoryContext, targetLanguage, nativeLanguage, state)

        // Assert
        assertTrue(result is MindReaderNextTurn.Guess)
        val guessResult = result as MindReaderNextTurn.Guess
        assertEquals("Cat", guessResult.guess.entity.resolveTranslation(targetLanguage))
        assertEquals("قطة", guessResult.guess.entity.resolveTranslation(nativeLanguage))
        assertEquals("🐱", guessResult.guess.entity.emoji)
    }

    @Test(expected = Exception::class)
    fun `invoke with error throws exception`() = runTest {
        // Arrange
        coEvery { 
            repository.getNextTurn(any(), any(), any(), any())
        } throws Exception("Network error")

        // Act
        useCase("test", "en", "ar", MindReaderGameState())
    }
}
