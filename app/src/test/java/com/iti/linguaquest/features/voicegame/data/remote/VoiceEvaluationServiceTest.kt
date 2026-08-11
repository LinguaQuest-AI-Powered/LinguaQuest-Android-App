package com.iti.linguaquest.features.voicegame.data.remote

import com.iti.linguaquest.core.ai.network.GeminiRestClient
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class VoiceEvaluationServiceTest {

    private lateinit var geminiRestClient: GeminiRestClient
    private lateinit var evaluationService: VoiceEvaluationService

    @Before
    fun setUp() {
        geminiRestClient = mockk()
        evaluationService = VoiceEvaluationService(geminiRestClient)
    }

    @Test
    fun evaluatePronunciation_convertsPcmToWavAndParsesAiEvaluation_whenGeminiReturnsValidJson() = runTest {
        // Given
        val targetSentence = "Hello world"
        val pcmAudioBytes = byteArrayOf(1, 2, 3, 4)
        val jsonResponse = """
            {
              "rating": 8,
              "correct_words": ["hello"],
              "wrong_words": ["world"],
              "advice": "Good effort!",
              "transcription": "hello"
            }
        """.trimIndent()

        coEvery {
            geminiRestClient.executeGeminiRequest(any())
        } returns jsonResponse

        // When
        val result = evaluationService.evaluatePronunciation(
            targetSentence = targetSentence,
            targetLanguage = "English",
            audioBytes = pcmAudioBytes,
            appLanguage = "English"
        )

        // Then
        assertEquals(5, result.rating)
        assertEquals(listOf("Hello"), result.correctWords)
        assertEquals(listOf("world"), result.wrongWords)
        assertEquals("Good effort!", result.advice)

        coVerify(exactly = 1) {
            geminiRestClient.executeGeminiRequest(
                match { request ->
                    val prompt = request.contents.firstOrNull()?.parts?.getOrNull(1)?.text ?: ""
                    prompt.contains("Hello world") && prompt.contains("English")
                }
            )
        }
    }

    @Test
    fun evaluatePronunciation_recalculatesWrongWords_basedOnTargetSentenceWords() = runTest {
        // Given
        val targetSentence = "The quick brown fox jumps"
        val pcmAudioBytes = byteArrayOf(10, 20)
        val jsonResponse = """
            {
              "rating": 6,
              "correct_words": ["The", "fox"],
              "wrong_words": ["quick", "brown"],
              "advice": "Practice quick and brown.",
              "transcription": "The fox"
            }
        """.trimIndent()

        coEvery {
            geminiRestClient.executeGeminiRequest(any())
        } returns jsonResponse

        // When
        val result = evaluationService.evaluatePronunciation(
            targetSentence = targetSentence,
            targetLanguage = "English",
            audioBytes = pcmAudioBytes,
            appLanguage = "English"
        )

        // Then
        assertEquals(4, result.rating)
        assertEquals(listOf("The", "fox"), result.correctWords)
        assertEquals(listOf("quick", "brown", "jumps"), result.wrongWords)
        assertEquals("Practice quick and brown.", result.advice)
    }

    @Test(expected = Exception::class)
    fun evaluatePronunciation_throwsException_whenGeminiReturnsNull() = runTest {
        // Given
        coEvery {
            geminiRestClient.executeGeminiRequest(any())
        } returns null

        // When
        evaluationService.evaluatePronunciation(
            targetSentence = "Bonjour",
            targetLanguage = "French",
            audioBytes = byteArrayOf(1, 2),
            appLanguage = "English"
        )
    }
}
