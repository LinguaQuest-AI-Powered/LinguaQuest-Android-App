package com.iti.linguaquest.features.voicegame.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.voicegame.domain.model.PronunciationSentence
import com.iti.linguaquest.features.voicegame.domain.repository.VoiceGameRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GeneratePronunciationSentenceUseCaseTest {

    private lateinit var repository: VoiceGameRepository
    private lateinit var useCase: GeneratePronunciationSentenceUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GeneratePronunciationSentenceUseCase(repository)
    }

    @Test
    fun invoke_returnsSuccessResult_whenRepositorySucceeds() = runTest {
        val expectedSentence = PronunciationSentence(
            sentence = "Hello world",
            difficulty = "Easy",
            phonetic = "/həˈloʊ wɜːrld/",
            translation = "Hola mundo"
        )
        coEvery {
            repository.generatePronunciationSentence("Spanish", "Intermediate", "Travel")
        } returns LinguaQuestResult.Success(expectedSentence)

        val result = useCase(targetLanguage = "Spanish", level = "Intermediate", topic = "Travel")

        assertTrue(result is LinguaQuestResult.Success)
        assertEquals(expectedSentence, (result as LinguaQuestResult.Success).data)
        coVerify(exactly = 1) {
            repository.generatePronunciationSentence("Spanish", "Intermediate", "Travel")
        }
    }

    @Test
    fun invoke_returnsFailureResult_whenRepositoryFails() = runTest {
        val expectedError = LinguaQuestDataError.CustomServerMessage("Failed to generate sentence")
        coEvery {
            repository.generatePronunciationSentence(any(), any(), any())
        } returns LinguaQuestResult.Failure(expectedError)

        val result = useCase("English", "Beginner", "General")

        assertTrue(result is LinguaQuestResult.Failure)
        assertEquals(expectedError, (result as LinguaQuestResult.Failure).error)
    }

    @Test
    fun invoke_usesDefaultArguments_whenNoneProvided() = runTest {
        val expectedSentence = PronunciationSentence("Test", "Easy", "test", "test")
        coEvery {
            repository.generatePronunciationSentence("English", "Beginner", "General Conversation")
        } returns LinguaQuestResult.Success(expectedSentence)

        val result = useCase()

        assertTrue(result is LinguaQuestResult.Success)
        coVerify(exactly = 1) {
            repository.generatePronunciationSentence("English", "Beginner", "General Conversation")
        }
    }
}
