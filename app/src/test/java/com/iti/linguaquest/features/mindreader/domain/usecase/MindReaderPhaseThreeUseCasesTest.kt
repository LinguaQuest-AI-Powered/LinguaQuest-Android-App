package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.LocalizedText
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAttribute
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderContradictionResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameConfig
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameHistory
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderHistoryEntry
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderRewardChallenge
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MindReaderPhaseThreeUseCasesTest {

    private val attributes = listOf(
        MindReaderAttribute("is_food", localizedText("Is it food?", "هل هو طعام؟")),
        MindReaderAttribute("is_animal", localizedText("Is it an animal?", "هل هو حيوان؟")),
        MindReaderAttribute("can_cut", localizedText("Can it cut?", "هل يمكنه القطع؟"))
    )

    private val apple = MindReaderEntity(
        id = "apple",
        worldKey = "kitchen",
        translations = localizedText("apple", "تفاحة"),
        emoji = "🍎",
        positiveAttributes = setOf("is_food")
    )

    private val knife = MindReaderEntity(
        id = "knife",
        worldKey = "kitchen",
        translations = localizedText("knife", "سكين"),
        emoji = "🔪",
        positiveAttributes = setOf("can_cut")
    )

    private val dog = MindReaderEntity(
        id = "dog",
        worldKey = "park",
        translations = localizedText("dog", "كلب"),
        emoji = "🐶",
        positiveAttributes = setOf("is_animal")
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

    @Test
    fun buildPopQuiz_returnsThreeChoicesAndKeepsTheCorrectOne() {
        val useCase = BuildMindReaderPopQuizQuestionUseCase()

        val question = useCase(
            correctEntity = apple,
            candidatePool = listOf(apple, knife, dog)
        )

        assertEquals(3, question.choices.size)
        assertTrue(question.choices.any { it.entity.id == "apple" })
        assertEquals("Tap the correct translation to claim your reward.", question.prompt.resolve("en"))
    }

    @Test
    fun contradictionCheck_detectsInconsistentAnswers() {
        val useCase = CheckMindReaderContradictionsUseCase()
        val history = MindReaderGameHistory(
            turns = listOf(
                MindReaderHistoryEntry(
                    attributeId = "is_food",
                    question = localizedText("Is it food?", "هل هو طعام؟"),
                    answer = MindReaderAnswerOption.YES,
                    confidenceAfterAnswer = 0.8
                ),
                MindReaderHistoryEntry(
                    attributeId = "is_animal",
                    question = localizedText("Is it an animal?", "هل هو حيوان؟"),
                    answer = MindReaderAnswerOption.YES,
                    confidenceAfterAnswer = 0.6
                )
            )
        )

        val result = useCase(
            history = history,
            evaluatedEntity = apple,
            attributes = attributes
        )

        assertEquals(2, result.totalCount)
        assertEquals(1, result.contradictionCount)
        assertFalse(result.isHonest)
    }

    @Test
    fun rewardValidation_grantsVictoryForHonestPopQuizAndStump() {
        val contradictionUseCase = CheckMindReaderContradictionsUseCase()
        val resolveRewardUseCase = ResolveMindReaderRewardUseCase()

        val honestHistory = MindReaderGameHistory(
            turns = listOf(
                MindReaderHistoryEntry(
                    attributeId = "is_food",
                    question = localizedText("Is it food?", "هل هو طعام؟"),
                    answer = MindReaderAnswerOption.YES,
                    confidenceAfterAnswer = 0.8
                )
            )
        )
        val honestContradictionResult = contradictionUseCase(
            history = honestHistory,
            evaluatedEntity = apple,
            attributes = attributes
        )

        val popQuizResult = resolveRewardUseCase(
            challenge = MindReaderRewardChallenge.PopQuiz(
                correctEntity = apple,
                selectedEntity = apple
            ),
            contradictionResult = honestContradictionResult,
            config = config,
            history = honestHistory
        )

        assertTrue(popQuizResult is MindReaderResult.Victory)

        val dogHistory = MindReaderGameHistory(
            turns = listOf(
                MindReaderHistoryEntry(
                    attributeId = "is_animal",
                    question = localizedText("Is it an animal?", "هل هو حيوان؟"),
                    answer = MindReaderAnswerOption.YES,
                    confidenceAfterAnswer = 0.8
                )
            )
        )
        val dogContradictionResult = contradictionUseCase(
            history = dogHistory,
            evaluatedEntity = dog,
            attributes = attributes
        )

        val stumpResult = resolveRewardUseCase(
            challenge = MindReaderRewardChallenge.Stump(selectedEntity = dog),
            contradictionResult = dogContradictionResult,
            config = config,
            history = dogHistory
        )

        assertTrue(stumpResult is MindReaderResult.Victory)
    }

    @Test
    fun rewardValidation_bustsWhenAnswersContradictTheChosenWord() {
        val contradictionUseCase = CheckMindReaderContradictionsUseCase()
        val resolveRewardUseCase = ResolveMindReaderRewardUseCase()

        val dishonestHistory = MindReaderGameHistory(
            turns = listOf(
                MindReaderHistoryEntry(
                    attributeId = "is_animal",
                    question = localizedText("Is it an animal?", "هل هو حيوان؟"),
                    answer = MindReaderAnswerOption.YES,
                    confidenceAfterAnswer = 0.2
                )
            )
        )
        val contradictionResult = contradictionUseCase(
            history = dishonestHistory,
            evaluatedEntity = apple,
            attributes = attributes
        )

        val result = resolveRewardUseCase(
            challenge = MindReaderRewardChallenge.Stump(selectedEntity = apple),
            contradictionResult = contradictionResult,
            config = config,
            history = dishonestHistory
        )

        assertTrue(result is MindReaderResult.Busted)
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
