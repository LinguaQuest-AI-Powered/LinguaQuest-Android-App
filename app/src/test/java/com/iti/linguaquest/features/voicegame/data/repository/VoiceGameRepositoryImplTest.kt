package com.iti.linguaquest.features.voicegame.data.repository

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.voicegame.data.model.VoiceEvaluationResponse
import com.iti.linguaquest.features.voicegame.data.datasource.remote.GeneratedSentence
import com.iti.linguaquest.features.voicegame.data.datasource.remote.PronunciationSentenceGeneratorService
import com.iti.linguaquest.features.voicegame.data.datasource.remote.VoiceEvaluationService
import com.iti.linguaquest.features.voicegame.domain.repository.VoiceGameRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VoiceGameRepositoryImplTest {

    private lateinit var evaluationService: VoiceEvaluationService
    private lateinit var generatorService: PronunciationSentenceGeneratorService
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var repository: VoiceGameRepository

    @Before
    fun setUp() {
        evaluationService = mockk()
        generatorService = mockk()
        userPreferencesRepository = mockk()
        repository = VoiceGameRepositoryImpl(
            service = evaluationService,
            generatorService = generatorService,
            userPreferencesRepository = userPreferencesRepository
        )
    }

    @Test
    fun evaluatePronunciation_returnsSuccess_whenServiceSucceedsWithProvidedAppLanguage() = runTest {
        val audioBytes = byteArrayOf(1, 2, 3)
        val serviceResponse = VoiceEvaluationResponse(
            rating = 8,
            correctWords = listOf("hello"),
            wrongWords = listOf("world"),
            advice = "Good attempt"
        )
        coEvery {
            evaluationService.evaluatePronunciation(
                targetSentence = "Hello world",
                targetLanguage = "English",
                audioBytes = audioBytes,
                appLanguage = "Spanish"
            )
        } returns serviceResponse

        val result = repository.evaluatePronunciation(
            targetSentence = "Hello world",
            targetLanguage = "English",
            audioBytes = audioBytes,
            appLanguage = "Spanish"
        )

        assertTrue(result is LinguaQuestResult.Success)
        val success = result as LinguaQuestResult.Success
        assertEquals(8, success.data.rating)
        assertEquals(listOf("hello"), success.data.correctWords)
        assertEquals(listOf("world"), success.data.wrongWords)
        assertEquals("Good attempt", success.data.advice)
    }

    @Test
    fun evaluatePronunciation_resolvesAppLanguageFromUserPreferences_whenAppLanguageIsNull() = runTest {
        val audioBytes = byteArrayOf(10, 20)
        coEvery { userPreferencesRepository.nativeLanguageName } returns flowOf("Arabic")
        coEvery { userPreferencesRepository.appLanguage } returns flowOf("ar")
        val serviceResponse = VoiceEvaluationResponse(
            rating = 10,
            correctWords = listOf("bonjour"),
            wrongWords = emptyList(),
            advice = "ممتاز"
        )
        coEvery {
            evaluationService.evaluatePronunciation(
                targetSentence = "Bonjour",
                targetLanguage = "French",
                audioBytes = audioBytes,
                appLanguage = "Arabic"
            )
        } returns serviceResponse

        val result = repository.evaluatePronunciation(
            targetSentence = "Bonjour",
            targetLanguage = "French",
            audioBytes = audioBytes,
            appLanguage = null
        )

        assertTrue(result is LinguaQuestResult.Success)
        coVerify(exactly = 1) {
            evaluationService.evaluatePronunciation("Bonjour", "French", audioBytes, "Arabic")
        }
    }

    @Test
    fun evaluatePronunciation_returnsFailure_whenServiceThrowsException() = runTest {
        val audioBytes = byteArrayOf(1, 1)
        coEvery { userPreferencesRepository.nativeLanguageName } returns flowOf(null)
        coEvery { userPreferencesRepository.appLanguage } returns flowOf("es")
        coEvery {
            evaluationService.evaluatePronunciation(any(), any(), any(), any())
        } throws RuntimeException("Network timeout")

        val result = repository.evaluatePronunciation("Hello", "English", audioBytes, null)

        assertTrue(result is LinguaQuestResult.Failure)
        val failure = result as LinguaQuestResult.Failure
        assertTrue(failure.error is LinguaQuestDataError.CustomServerMessage)
        assertEquals("Network timeout", (failure.error as LinguaQuestDataError.CustomServerMessage).message)
    }

    @Test
    fun generatePronunciationSentence_returnsSuccess_whenGeneratorServiceReturnsSentences() = runTest {
        val generatedSentences = listOf(
            GeneratedSentence(
                sentence = "Como estas",
                difficulty = "Easy",
                phonetic = "/ˈko.mo esˈtas/",
                translation = "How are you"
            )
        )
        coEvery {
            generatorService.generateSentences("Spanish", "Beginner", "Greetings", 1)
        } returns generatedSentences

        val result = repository.generatePronunciationSentence("Spanish", "Beginner", "Greetings")

        assertTrue(result is LinguaQuestResult.Success)
        val success = result as LinguaQuestResult.Success
        assertEquals("Como estas", success.data.sentence)
        assertEquals("Easy", success.data.difficulty)
        assertEquals("/ˈko.mo esˈtas/", success.data.phonetic)
        assertEquals("How are you", success.data.translation)
    }

    @Test
    fun generatePronunciationSentence_returnsFailure_whenGeneratorServiceThrowsException() = runTest {
        coEvery {
            generatorService.generateSentences(any(), any(), any(), any())
        } throws RuntimeException("AI Model Busy")

        val result = repository.generatePronunciationSentence("English", "Beginner", "General")

        assertTrue(result is LinguaQuestResult.Failure)
        val failure = result as LinguaQuestResult.Failure
        assertEquals("AI Model Busy", (failure.error as LinguaQuestDataError.CustomServerMessage).message)
    }
}
