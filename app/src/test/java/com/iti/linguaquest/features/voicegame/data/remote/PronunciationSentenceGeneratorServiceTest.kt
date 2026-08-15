package com.iti.linguaquest.features.voicegame.data.remote

import com.iti.linguaquest.core.ai.client.AiClient
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

    private lateinit var aiClient: AiClient
    private lateinit var generatorService: PronunciationSentenceGeneratorService

    @Before
    fun setUp() {
        aiClient = mockk()
        generatorService = PronunciationSentenceGeneratorService(aiClient)
    }

    @Test
    fun generateSentences_returnsParsedSentences_whenGeminiReturnsValidJsonObjectWithSentencesArray() = runTest {
        // Given
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

        coEvery { aiClient.generateJson(any(), any()) } returns jsonResponse

        // When
        val result = generatorService.generateSentences(
            targetLanguage = "Spanish",
            level = "Beginner",
            topic = "Greetings",
            count = 2
        )

        // Then
        assertEquals(2, result.size)
        assertEquals("Hola, ¿cómo estás?", result[0].sentence)
        assertEquals("Easy", result[0].difficulty)
        assertEquals("/ˈo.la ˈko.mo esˈtas/", result[0].phonetic)
        assertEquals("Hello, how are you?", result[0].translation)

        assertEquals("Buenos días", result[1].sentence)
        assertEquals("Easy", result[1].difficulty)

        coVerify(exactly = 1) { aiClient.generateJson(any(), any()) }
    }

    @Test
    fun generateSentences_returnsParsedSentence_whenGeminiReturnsSingleJsonObject() = runTest {
        // Given
        val jsonResponse = """
            {
              "sentence": "Wie geht es dir?",
              "difficulty": "Easy",
              "phonetic": "/viː ɡeːt ɛs diːɐ̯/",
              "translation": "How are you?"
            }
        """.trimIndent()

        coEvery { aiClient.generateJson(any(), any()) } returns jsonResponse

        // When
        val result = generatorService.generateSentences(
            targetLanguage = "German",
            level = "Beginner",
            topic = "General",
            count = 1
        )

        // Then
        assertEquals(1, result.size)
        assertEquals("Wie geht es dir?", result[0].sentence)
        assertEquals("Easy", result[0].difficulty)
        assertEquals("/viː ɡeːt ɛs diːɐ̯/", result[0].phonetic)
        assertEquals("How are you?", result[0].translation)
    }

    @Test
    fun generateSentences_returnsFallback_whenGeminiReturnsNull() = runTest {
        // Given
        coEvery { aiClient.generateJson(any(), any()) } returns null

        // When
        val result = generatorService.generateSentences(
            targetLanguage = "Spanish",
            level = "Beginner",
            topic = "Travel",
            count = 3
        )

        // Then
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun generateSentences_returnsFallback_whenGeminiReturnsInvalidJson() = runTest {
        // Given
        coEvery { aiClient.generateJson(any(), any()) } returns "INVALID_JSON_RESPONSE"

        // When
        val result = generatorService.generateSentences(
            targetLanguage = "French",
            level = "Intermediate",
            topic = "Food",
            count = 2
        )

        // Then
        assertTrue(result.isNotEmpty())
    }
}
