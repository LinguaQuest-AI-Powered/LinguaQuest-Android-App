package com.iti.linguaquest.features.voicegame.data.remote

import com.iti.linguaquest.core.ai.GeminiAiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class VoiceEvaluationServiceTest {

    private lateinit var geminiAiService: GeminiAiService
    private lateinit var evaluationService: VoiceEvaluationService

    @Before
    fun setUp() {
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
            geminiAiService.generateJsonFromAudio(
                prompt = any(),
                audioBytes = any(),
                mimeType = eq("audio/wav")
            )
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
            geminiAiService.generateJsonFromAudio(
                prompt = match { it.contains("Hello world") && it.contains("English") },
                audioBytes = match { it.size == 44 + pcmAudioBytes.size },
                mimeType = "audio/wav"
            )
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
            geminiAiService.generateJsonFromAudio(any(), any(), any())
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
            geminiAiService.generateJsonFromAudio(any(), any(), any())
        } returns null

        evaluationService.evaluatePronunciation(
            targetSentence = "Bonjour",
            targetLanguage = "French",
            audioBytes = byteArrayOf(1, 2),
            appLanguage = "English"
        )
    }
}
