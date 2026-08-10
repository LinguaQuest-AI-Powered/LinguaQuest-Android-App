package com.iti.linguaquest.features.voicegame.data.remote

import com.iti.linguaquest.core.ai.network.GeminiRestClient
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class VoiceEvaluationServiceTest {

    private lateinit var geminiAiService: GeminiRestClient
    private lateinit var evaluationService: VoiceEvaluationService

    @Before
    fun setUp() {
        mockkStatic(android.util.Base64::class)
        io.mockk.every { android.util.Base64.encodeToString(any(), any()) } returns "mocked_base64"
        geminiAiService = mockk()
        evaluationService = VoiceEvaluationService(geminiAiService)
    }

    @Test
    fun evaluatePronunciation_convertsPcmToWavAndParsesAiEvaluation_whenGeminiReturnsValidJson() = runTest {
        val targetSentence = "Hello world"
        val pcmAudioBytes = byteArrayOf(1, 2, 3, 4)
        val jsonResponse = """
            {
              "rating": 8,
              "correct_words": ["hello"],
              "wrong_words": ["world"],
              "advice": "Good effort!"
            }
        """.trimIndent()

        coEvery {
            geminiAiService.executeGeminiRequest(any())
        } returns jsonResponse

        val result = evaluationService.evaluatePronunciation(
            targetSentence = targetSentence,
            targetLanguage = "English",
            audioBytes = pcmAudioBytes,
            appLanguage = "English"
        )

        assertEquals(8, result.rating)
        assertEquals(listOf("Hello"), result.correctWords)
        assertEquals(listOf("world"), result.wrongWords)
        assertEquals("Good effort!", result.advice)

        coVerify(exactly = 1) {
            geminiAiService.executeGeminiRequest(any())
        }
    }

    @Test
    fun evaluatePronunciation_recalculatesWrongWords_basedOnTargetSentenceWords() = runTest {
        val targetSentence = "The quick brown fox jumps"
        val pcmAudioBytes = byteArrayOf(10, 20)
        val jsonResponse = """
            {
              "rating": 6,
              "correct_words": ["The", "fox"],
              "wrong_words": ["quick", "brown"],
              "advice": "Practice quick and brown."
            }
        """.trimIndent()

        coEvery {
            geminiAiService.executeGeminiRequest(any())
        } returns jsonResponse

        val result = evaluationService.evaluatePronunciation(
            targetSentence = targetSentence,
            targetLanguage = "English",
            audioBytes = pcmAudioBytes,
            appLanguage = "English"
        )

        assertEquals(6, result.rating)
        assertEquals(listOf("The", "fox"), result.correctWords)
        assertEquals(listOf("quick", "brown", "jumps"), result.wrongWords)
        assertEquals("Practice quick and brown.", result.advice)
    }

    @Test(expected = Exception::class)
    fun evaluatePronunciation_throwsException_whenGeminiReturnsNull() = runTest {
        coEvery {
            geminiAiService.executeGeminiRequest(any())
        } returns null

        evaluationService.evaluatePronunciation(
            targetSentence = "Bonjour",
            targetLanguage = "French",
            audioBytes = byteArrayOf(1, 2),
            appLanguage = "English"
        )
    }
}
