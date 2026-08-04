package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.data.datasource.remote.MindReaderAiResponse
import com.iti.linguaquest.features.mindreader.data.datasource.remote.MindReaderAiService
import com.iti.linguaquest.features.mindreader.data.datasource.remote.MindReaderStumpVerificationResponse
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.domain.model.LocalizedText
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameHistory
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderHistoryEntry
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.UUID

class VerifyMindReaderHonestyUseCaseTest {

    private lateinit var useCase: VerifyMindReaderHonestyUseCase
    private lateinit var aiService: FakeMindReaderAiService

    class FakeGeminiAiService : com.iti.linguaquest.core.ai.GeminiAiService() {
        override suspend fun generateJson(prompt: String): String? {
            return null
        }
        override suspend fun generateJsonFromAudio(prompt: String, audioBytes: ByteArray, mimeType: String): String? {
            return null
        }
    }

    class FakeMindReaderAiService : MindReaderAiService(FakeGeminiAiService(), com.google.gson.Gson()) {
        var mockedResponse: MindReaderStumpVerificationResponse? = null
        
        override suspend fun verifyUserWord(
            category: String,
            history: String,
            userWord: String
        ): MindReaderStumpVerificationResponse? {
            return mockedResponse
        }

        override suspend fun getNextTurn(
            category: String,
            targetLanguage: String,
            nativeLanguage: String,
            history: String
        ): MindReaderAiResponse? {
            return null
        }
    }

    @Before
    fun setup() {
        aiService = FakeMindReaderAiService()
        useCase = VerifyMindReaderHonestyUseCase(aiService)
    }

    @Test
    fun `invoke returns honest result when AI verifies honesty`() = runBlocking {
        // Arrange
        val aiResponse = MindReaderStumpVerificationResponse(
            isHonest = true,
            reason = "The user answers match the word."
        )
        aiService.mockedResponse = aiResponse

        val history = MindReaderGameHistory(
            turns = listOf(
                MindReaderHistoryEntry(
                    attributeId = "1",
                    question = LocalizedText(mapOf("en" to "Is it an animal?")),
                    answer = MindReaderAnswerOption.YES,
                    confidenceAfterAnswer = 1.0
                )
            )
        )
        val userWord = MindReaderEntity(
            id = UUID.randomUUID().toString(),
            worldKey = "nature",
            translations = LocalizedText(mapOf("en" to "Cat", "ar" to "قطة")),
            emoji = "🐱",
            positiveAttributes = emptySet()
        )

        // Act
        val result = useCase(
            category = "nature",
            targetLanguage = "en",
            history = history,
            userWord = userWord
        )

        // Assert
        assertEquals(0, result.contradictionCount)
        assertEquals(1, result.matchedCount)
        assertEquals(1, result.totalCount)
        assertEquals("The user answers match the word.", result.reason)
        assertEquals(userWord, result.evaluatedEntity)
    }

    @Test
    fun `invoke returns dishonest result when AI detects contradiction`() = runBlocking {
        // Arrange
        val aiResponse = MindReaderStumpVerificationResponse(
            isHonest = false,
            reason = "User said no to animal, but cat is an animal."
        )
        aiService.mockedResponse = aiResponse

        val history = MindReaderGameHistory(
            turns = listOf(
                MindReaderHistoryEntry(
                    attributeId = "1",
                    question = LocalizedText(mapOf("en" to "Is it an animal?")),
                    answer = MindReaderAnswerOption.NO,
                    confidenceAfterAnswer = 1.0
                )
            )
        )
        val userWord = MindReaderEntity(
            id = UUID.randomUUID().toString(),
            worldKey = "nature",
            translations = LocalizedText(mapOf("en" to "Cat", "ar" to "قطة")),
            emoji = "🐱",
            positiveAttributes = emptySet()
        )

        // Act
        val result = useCase(
            category = "nature",
            targetLanguage = "en",
            history = history,
            userWord = userWord
        )

        // Assert
        assertEquals(1, result.contradictionCount)
        assertEquals(1, result.matchedCount)
        assertEquals(1, result.totalCount)
        assertEquals("User said no to animal, but cat is an animal.", result.reason)
    }

    @Test
    fun `invoke returns honest fallback result when AI fails`() = runBlocking {
        // Arrange
        aiService.mockedResponse = null

        val history = MindReaderGameHistory(
            turns = listOf(
                MindReaderHistoryEntry(
                    attributeId = "1",
                    question = LocalizedText(mapOf("en" to "Is it big?")),
                    answer = MindReaderAnswerOption.YES,
                    confidenceAfterAnswer = 1.0
                )
            )
        )
        val userWord = MindReaderEntity(
            id = UUID.randomUUID().toString(),
            worldKey = "nature",
            translations = LocalizedText(mapOf("en" to "Elephant", "ar" to "فيل")),
            emoji = "🐘",
            positiveAttributes = emptySet()
        )

        // Act
        val result = useCase(
            category = "nature",
            targetLanguage = "en",
            history = history,
            userWord = userWord
        )

        // Assert
        assertEquals(0, result.contradictionCount)
        assertTrue(result.reason?.contains("AI Verification failed") ?: false)
    }
}
