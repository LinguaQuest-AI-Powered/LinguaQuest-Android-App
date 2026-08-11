package com.iti.linguaquest.features.voicegame.data.remote

import com.iti.linguaquest.core.ai.GeminiAiService
import com.iti.linguaquest.features.voicegame.data.datasource.remote.PronunciationSentenceGeneratorService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PronunciationSentenceGeneratorServiceTest {

    private lateinit var geminiAiService: GeminiAiService
    private lateinit var generatorService: PronunciationSentenceGeneratorService

    @Before
    fun setUp() {
        geminiAiService = mockk()
        generatorService = PronunciationSentenceGeneratorService(geminiAiService)
    }

    @Test
    fun generateSentences_returnsParsedSentences_whenGeminiReturnsValidJsonObjectWithSentencesArray() = runTest {
        val jsonResponse = """
            {
              "sentences": [
                {
                  "sentence": "Hola, ¿cómo estás?",
                  "difficulty": "Easy",
                  "phonetic": "/ˈo.la ˈko.mo esˈtas/",
                  "translation": "Hello, how are you?"
                },
                {
                  "sentence": "Buenos días",
                  "difficulty": "Easy",
                  "phonetic": "/ˈbwe.nos ˈdi.as/",
                  "translation": "Good morning"
                }
              ]
            }
        """.trimIndent()

        coEvery { geminiAiService.generateJson(any()) } returns jsonResponse

        val result = generatorService.generateSentences(
            targetLanguage = "Spanish",
            level = "Beginner",
            topic = "Greetings",
            count = 2
        )

        assertEquals(2, result.size)
        assertEquals("Hola, ¿cómo estás?", result[0].sentence)
        assertEquals("Easy", result[0].difficulty)
        assertEquals("/ˈo.la ˈko.mo esˈtas/", result[0].phonetic)
        assertEquals("Hello, how are you?", result[0].translation)

        assertEquals("Buenos días", result[1].sentence)
        assertEquals("Easy", result[1].difficulty)

        coVerify(exactly = 1) { geminiAiService.generateJson(any()) }
    }

    @Test
    fun generateSentences_returnsParsedSentence_whenGeminiReturnsSingleJsonObject() = runTest {
        val jsonResponse = """
            {
              "sentence": "Wie geht es dir?",
              "difficulty": "Easy",
              "phonetic": "/viː ɡeːt ɛs diːɐ̯/",
              "translation": "How are you?"
            }
        """.trimIndent()

        coEvery { geminiAiService.generateJson(any()) } returns jsonResponse

        val result = generatorService.generateSentences(
            targetLanguage = "German",
            level = "Beginner",
            topic = "General",
            count = 1
        )

        assertEquals(1, result.size)
        assertEquals("Wie geht es dir?", result[0].sentence)
        assertEquals("Easy", result[0].difficulty)
        assertEquals("/viː ɡeːt ɛs diːɐ̯/", result[0].phonetic)
        assertEquals("How are you?", result[0].translation)
    }

    @Test
    fun generateSentences_returnsFallback_whenGeminiReturnsNull() = runTest {
        coEvery { geminiAiService.generateJson(any()) } returns null

        val result = generatorService.generateSentences(
            targetLanguage = "Spanish",
            level = "Beginner",
            topic = "General",
            count = 2
        )

        assertEquals(2, result.size)
        assertTrue(result.all { it.sentence.isNotBlank() })
    }

    @Test
    fun generateSentences_returnsFallback_whenGeminiReturnsInvalidJson() = runTest {
        coEvery { geminiAiService.generateJson(any()) } returns "INVALID_JSON_RESPONSE"

        val result = generatorService.generateSentences(
            targetLanguage = "French",
            level = "Beginner",
            topic = "General",
            count = 3
        )

        assertEquals(3, result.size)
        assertTrue(result.all { it.sentence.isNotBlank() })
    }
}
