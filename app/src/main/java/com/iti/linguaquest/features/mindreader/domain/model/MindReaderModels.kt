package com.iti.linguaquest.features.mindreader.domain.model

import com.iti.linguaquest.core.domain.model.MiniGameReward
import com.iti.linguaquest.core.sharedComponents.text.UiText
import java.util.Locale

data class MindReaderEntity(
    val id: String,
    val worldKey: String,
    val targetText: String,
    val nativeText: String = "",
    val emoji: String
)

data class MindReaderGameConfig(
    val maxQuestions: Int = 20,
    val guessThreshold: Double = 0.9,
    val translationCost: Int = 5,
    val correctRewardCoins: Int = MiniGameReward.MIND_READER_VICTORY.coins,
    val correctRewardXp: Int = MiniGameReward.MIND_READER_VICTORY.xp,
    val stumpBonusCoins: Int = MiniGameReward.MIND_READER_STUMP.coins,
    val stumpBonusXp: Int = MiniGameReward.MIND_READER_STUMP.xp
)

enum class MindReaderAnswerOption(val rawId: String) {
    YES("yes"),
    PROBABLY("probably"),
    SOMETIMES("sometimes"),
    PROBABLY_NOT("probably_not"),
    NO("no");

    fun resolveLabel(languageCode: String?): String {
        val code = languageCode.orEmpty().trim().lowercase(Locale.ROOT).take(2)
        return when (this) {
            YES -> when (code) {
                "ar" -> "نعم"
                "es" -> "Sí"
                "de" -> "Ja"
                "fr" -> "Oui"
                "it" -> "Sì"
                "pt" -> "Sim"
                else -> "Yes"
            }
            NO -> when (code) {
                "ar" -> "لا"
                "es" -> "No"
                "de" -> "Nein"
                "fr" -> "Non"
                "it" -> "No"
                "pt" -> "Não"
                else -> "No"
            }
            SOMETIMES -> when (code) {
                "ar" -> "أحياناً"
                "es" -> "A veces"
                "de" -> "Manchmal"
                "fr" -> "Parfois"
                "it" -> "A volte"
                "pt" -> "Às vezes"
                else -> "Sometimes"
            }
            PROBABLY_NOT -> when (code) {
                "ar" -> "على الأغلب لا"
                "es" -> "Probablemente no"
                "de" -> "Wahrscheinlich nicht"
                "fr" -> "Probablement pas"
                "it" -> "Probabilmente no"
                "pt" -> "Provavelmente não"
                else -> "Probably Not"
            }
            PROBABLY -> when (code) {
                "ar" -> "لا أعلم"
                "es" -> "No sé"
                "de" -> "Ich weiß nicht"
                "fr" -> "Je ne sais pas"
                "it" -> "Non lo so"
                "pt" -> "Não sei"
                else -> "Don't Know"
            }
        }
    }

    companion object {
        fun fromRawId(rawId: String?): MindReaderAnswerOption? {
            val normalized = rawId?.trim().orEmpty()
            if (normalized.isEmpty()) return null
            return entries.firstOrNull { it.rawId.equals(normalized, ignoreCase = true) }
        }
    }
}

data class MindReaderPopQuizChoice(
    val entity: MindReaderEntity
)

data class MindReaderPopQuizQuestion(
    val correctEntity: MindReaderEntity,
    val choices: List<MindReaderPopQuizChoice>
) {
    fun isCorrect(choice: MindReaderPopQuizChoice): Boolean {
        return choice.entity.id == correctEntity.id
    }
}

data class MindReaderContradictionDetail(
    val attributeId: String,
    val questionTargetText: String,
    val answer: MindReaderAnswerOption,
    val actualHasAttribute: Boolean,
    val isContradiction: Boolean
)

data class MindReaderContradictionResult(
    val evaluatedEntity: MindReaderEntity,
    val details: List<MindReaderContradictionDetail>,
    val contradictionCount: Int,
    val matchedCount: Int,
    val totalCount: Int,
    val reason: String? = null
) {
    val isHonest: Boolean get() = contradictionCount == 0
}

data class MindReaderGuessResult(
    val entity: MindReaderEntity,
    val confidence: Double,
    val quizChoices: List<MindReaderAiQuizChoice> = emptyList()
)

data class MindReaderHistoryEntry(
    val attributeId: String,
    val questionTargetText: String,
    val questionNativeText: String,
    val answer: MindReaderAnswerOption,
    val confidenceAfterAnswer: Double
)

data class MindReaderGameHistory(
    val turns: List<MindReaderHistoryEntry> = emptyList()
) {
    fun append(turn: MindReaderHistoryEntry): MindReaderGameHistory {
        return copy(turns = turns + turn)
    }
}

data class MindReaderQuestionCandidate(
    val attributeId: String,
    val targetText: String,
    val nativeText: String
)

data class MindReaderGameState(
    val worldKey: String? = null,
    val askedAttributes: Set<String> = emptySet(),
    val questionCount: Int = 0,
    val history: MindReaderGameHistory = MindReaderGameHistory(),
    val pendingGuess: MindReaderGuessResult? = null
)

data class MindReaderGameLaunch(
    val languageCode: String,
    val nativeLanguageCode: String = "en",
    val config: MindReaderGameConfig,
    val state: MindReaderGameState
)

sealed interface MindReaderAiNextTurn {
    data class Question(
        val targetText: String,
        val nativeText: String
    ) : MindReaderAiNextTurn

    data class Guess(
        val word: String,
        val translation: String,
        val emoji: String,
        val quizChoices: List<MindReaderAiQuizChoice> = emptyList()
    ) : MindReaderAiNextTurn

    data object Error : MindReaderAiNextTurn
}

sealed interface MindReaderNextTurn {
    data class Question(val question: MindReaderQuestionCandidate) : MindReaderNextTurn
    data class Guess(val guess: MindReaderGuessResult) : MindReaderNextTurn
    data object Error : MindReaderNextTurn
}

data class MindReaderAiQuizChoice(
    val translationText: String,
    val isCorrect: Boolean
)

data class MindReaderAiHonestyResult(
    val isHonest: Boolean,
    val explanation: String
)

sealed interface MindReaderRewardChallenge {
    data class PopQuiz(
        val correctEntity: MindReaderEntity,
        val selectedEntity: MindReaderEntity
    ) : MindReaderRewardChallenge

    data class Stump(
        val selectedEntity: MindReaderEntity
    ) : MindReaderRewardChallenge
}

sealed interface MindReaderResult {
    data object Playing : MindReaderResult

    data class PopQuiz(
        val question: MindReaderPopQuizQuestion,
        val originalGuess: MindReaderGuessResult
    ) : MindReaderResult

    data class Stump(
        val candidates: List<MindReaderEntity>,
        val originalGuess: MindReaderGuessResult
    ) : MindReaderResult

    data class Guessing(
        val guess: MindReaderGuessResult
    ) : MindReaderResult

    data class Victory(
        val guess: MindReaderGuessResult,
        val history: MindReaderGameHistory,
        val rewardCoins: Int,
        val rewardXp: Int
    ) : MindReaderResult

    data class Busted(
        val guess: MindReaderGuessResult,
        val history: MindReaderGameHistory,
        val reason: UiText? = null
    ) : MindReaderResult

    data class Timeout(
        val guess: MindReaderGuessResult?,
        val history: MindReaderGameHistory
    ) : MindReaderResult
}
