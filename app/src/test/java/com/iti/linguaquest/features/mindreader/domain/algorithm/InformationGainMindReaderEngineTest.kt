package com.iti.linguaquest.features.mindreader.domain.algorithm

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAttribute
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameConfig
import com.iti.linguaquest.features.mindreader.domain.model.LocalizedText
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class InformationGainMindReaderEngineTest {

    private val attributes = listOf(
        MindReaderAttribute("is_food", localizedText("Is it food?", "هل هو طعام؟")),
        MindReaderAttribute("is_animal", localizedText("Is it an animal?", "هل هو حيوان؟")),
        MindReaderAttribute("can_fly", localizedText("Can it fly?", "هل يمكنه الطيران؟")),
        MindReaderAttribute("can_cut", localizedText("Can it cut things?", "هل يمكنه القطع؟"))
    )

    private val entities = listOf(
        MindReaderEntity(
            id = "apple",
            worldKey = "kitchen",
            translations = localizedText("apple", "تفاحة"),
            emoji = "🍎",
            positiveAttributes = setOf("is_food")
        ),
        MindReaderEntity(
            id = "knife",
            worldKey = "kitchen",
            translations = localizedText("knife", "سكين"),
            emoji = "🔪",
            positiveAttributes = setOf("can_cut")
        ),
        MindReaderEntity(
            id = "dog",
            worldKey = "park",
            translations = localizedText("dog", "كلب"),
            emoji = "🐶",
            positiveAttributes = setOf("is_animal")
        ),
        MindReaderEntity(
            id = "bird",
            worldKey = "park",
            translations = localizedText("bird", "طائر"),
            emoji = "🐦",
            positiveAttributes = setOf("is_animal", "can_fly")
        )
    )

    private val config = MindReaderGameConfig(
        maxQuestions = 20,
        guessThreshold = 0.9,
        translationCost = 5,
        correctRewardCoins = 20,
        correctRewardXp = 40,
        stumpBonusCoins = 100,
        stumpBonusXp = 80
    )

    private val engine = InformationGainMindReaderEngine(attributes, config)

    @Test
    fun nextQuestion_prefersTheMostBalancedSplit() {
        val state = engine.createSession(entities)

        val question = engine.nextQuestion(state)

        assertEquals("is_animal", question?.attributeId)
        assertEquals("Is it an animal?", question?.question?.resolve("en"))
    }

    @Test
    fun applyAnswer_sharpensGuessUntilThresholdIsMet() {
        val initialState = engine.createSession(entities)

        val afterAnimal = engine.applyAnswer(
            state = initialState,
            attributeId = "is_animal",
            answer = MindReaderAnswerOption.YES
        )

        val followUpQuestion = engine.nextQuestion(afterAnimal)
        assertEquals("can_fly", followUpQuestion?.attributeId)
        assertEquals("Can it fly?", followUpQuestion?.question?.resolve("en"))
        assertEquals(1, afterAnimal.history.turns.size)
        assertEquals("is_animal", afterAnimal.history.turns.first().attributeId)
        assertEquals(MindReaderAnswerOption.YES, afterAnimal.history.turns.first().answer)

        val afterFly = engine.applyAnswer(
            state = afterAnimal,
            attributeId = "can_fly",
            answer = MindReaderAnswerOption.YES
        )

        val guess = engine.currentGuess(afterFly)

        assertEquals("bird", guess?.entity?.id)
        assertTrue((guess?.confidence ?: 0.0) > 0.9)
        assertTrue(engine.shouldRevealGuess(afterFly))
        assertTrue(engine.guessProbability(afterFly) > 0.9)

        when (val result = engine.currentResult(afterFly)) {
            is MindReaderResult.Guessing -> assertEquals("bird", result.guess.entity.id)
            else -> throw AssertionError("Expected Guessing state, got $result")
        }
    }

    @Test
    fun currentResult_revealsGuessWhenNoUsefulQuestionsRemain() {
        val limitedAttributes = listOf(
            MindReaderAttribute("is_food", localizedText("Is it food?", "Ù‡Ù„ Ù‡Ùˆ Ø·Ø¹Ø§Ù…ØŸ"))
        )
        val limitedEngine = InformationGainMindReaderEngine(limitedAttributes, config)
        val sameCandidates = listOf(
            MindReaderEntity(
                id = "apple",
                worldKey = "kitchen",
                translations = localizedText("apple", "ØªÙØ§Ø­Ø©"),
                emoji = "ðŸŽ",
                positiveAttributes = setOf("is_food")
            ),
            MindReaderEntity(
                id = "orange",
                worldKey = "kitchen",
                translations = localizedText("orange", "Ø¨Ø±ØªÙ‚Ø§Ù„Ø©"),
                emoji = "ðŸŠ",
                positiveAttributes = setOf("is_food")
            )
        )

        val state = limitedEngine.createSession(sameCandidates)

        assertEquals(null, limitedEngine.nextQuestion(state))
        assertTrue(limitedEngine.shouldRevealGuess(state))

        when (val result = limitedEngine.currentResult(state)) {
            is MindReaderResult.Guessing -> assertTrue(result.guess.entity.id in setOf("apple", "orange"))
            else -> throw AssertionError("Expected Guessing state, got $result")
        }
    }

    private fun localizedText(en: String, ar: String): LocalizedText {
        return LocalizedText(
            mapOf(
                "en" to en,
                "ar" to ar
            )
        )
    }
}
