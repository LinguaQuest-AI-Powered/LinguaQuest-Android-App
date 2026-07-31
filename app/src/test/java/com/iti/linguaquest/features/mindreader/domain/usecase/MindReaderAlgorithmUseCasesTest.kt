package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.LocalizedText
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAttribute
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderDataset
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameConfig
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MindReaderAlgorithmUseCasesTest {

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

    private val dataset = MindReaderDataset(
        attributes = attributes,
        entities = entities,
        config = config
    )

    @Test
    fun algorithmUseCases_matchEngineFlow() {
        val createSessionUseCase = CreateMindReaderSessionUseCase()
        val getNextQuestionUseCase = GetMindReaderNextQuestionUseCase()
        val submitAnswerUseCase = SubmitMindReaderAnswerUseCase()
        val getCurrentGuessUseCase = GetMindReaderCurrentGuessUseCase()
        val shouldRevealGuessUseCase = ShouldRevealMindReaderGuessUseCase()
        val getCurrentResultUseCase = GetMindReaderCurrentResultUseCase()
        val getRankedQuestionsUseCase = GetMindReaderRankedQuestionsUseCase()
        val getRemainingQuestionsUseCase = GetMindReaderRemainingQuestionsUseCase()
        val getGuessProbabilityUseCase = GetMindReaderGuessProbabilityUseCase()

        val session = createSessionUseCase(
            dataset = dataset
        )

        val firstQuestion = getNextQuestionUseCase(
            dataset = dataset,
            state = session
        )

        assertEquals("is_animal", firstQuestion?.attributeId)
        assertTrue(getRankedQuestionsUseCase(dataset = dataset, state = session).isNotEmpty())
        assertEquals(20, getRemainingQuestionsUseCase(dataset = dataset, state = session))

        val afterAnimal = submitAnswerUseCase(
            dataset = dataset,
            state = session,
            attributeId = "is_animal",
            answer = MindReaderAnswerOption.YES
        )

        val nextQuestion = getNextQuestionUseCase(
            dataset = dataset,
            state = afterAnimal
        )

        assertEquals("can_fly", nextQuestion?.attributeId)

        val afterFly = submitAnswerUseCase(
            dataset = dataset,
            state = afterAnimal,
            attributeId = "can_fly",
            answer = MindReaderAnswerOption.YES
        )

        val guess = getCurrentGuessUseCase(
            dataset = dataset,
            state = afterFly
        )

        assertEquals("bird", guess?.entity?.id)
        assertTrue((guess?.confidence ?: 0.0) > 0.9)
        assertTrue(getGuessProbabilityUseCase(dataset = dataset, state = afterFly) > 0.9)
        assertTrue(shouldRevealGuessUseCase(dataset = dataset, state = afterFly))

        when (val result = getCurrentResultUseCase(dataset = dataset, state = afterFly)) {
            is MindReaderResult.Guessing -> assertEquals("bird", result.guess.entity.id)
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
