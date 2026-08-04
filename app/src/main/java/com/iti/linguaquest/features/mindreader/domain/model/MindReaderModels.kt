package com.iti.linguaquest.features.mindreader.domain.model

import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.util.Locale

@JsonAdapter(LocalizedTextAdapter::class)
data class LocalizedText(
    val values: Map<String, String>
) {
    fun resolve(languageCode: String?, fallbackLanguage: String = "en"): String {
        val normalizedLanguage = languageCode.orEmpty().trim().lowercase(Locale.ROOT)
        val normalizedFallback = fallbackLanguage.trim().lowercase(Locale.ROOT)

        if (normalizedLanguage.isNotEmpty()) {
            values[normalizedLanguage]?.let { return it }
            normalizedLanguage
                .takeIf { it.contains('-') || it.contains('_') }
                ?.let { code ->
                    val baseCode = code.substringBefore('-').substringBefore('_')
                    values[baseCode]?.let { return it }
                }
        }

        values[normalizedFallback]?.let { return it }
        return values.entries.firstOrNull()?.value.orEmpty()
    }
}

class LocalizedTextAdapter :  TypeAdapter<LocalizedText>() {
    override fun write(out: JsonWriter, value: LocalizedText?) {
        out.beginObject()
        value?.values.orEmpty().forEach { (languageCode, text) ->
            out.name(languageCode)
            out.value(text)
        }
        out.endObject()
    }

    override fun read(`in`: JsonReader): LocalizedText {
        if (`in`.peek() == JsonToken.NULL) {
            `in`.nextNull()
            return LocalizedText(emptyMap())
        }

        val values = linkedMapOf<String, String>()
        `in`.beginObject()
        while (`in`.hasNext()) {
            val name = `in`.nextName()
            val value = if (`in`.peek() == JsonToken.NULL) {
                `in`.nextNull()
                ""
            } else {
                `in`.nextString()
            }
            values[name.lowercase(Locale.ROOT)] = value
        }
        `in`.endObject()
        return LocalizedText(values)
    }
}

data class MindReaderAttribute(
    val id: String,
    val question: LocalizedText
)

data class MindReaderEntity(
    val id: String,
    val worldKey: String,
    val translations: LocalizedText,
    val emoji: String,
    val positiveAttributes: Set<String>
) {
    fun hasAttribute(attributeId: String): Boolean = attributeId in positiveAttributes

    fun resolveTranslation(languageCode: String?): String = translations.resolve(languageCode)
}

data class MindReaderGameConfig(
    val maxQuestions: Int,
    val guessThreshold: Double,
    val translationCost: Int,
    val correctRewardCoins: Int,
    val correctRewardXp: Int,
    val stumpBonusCoins: Int,
    val stumpBonusXp: Int
)

enum class MindReaderAnswerOption(val rawId: String) {
    YES("yes"),
    PROBABLY("probably"),
    SOMETIMES("sometimes"),
    PROBABLY_NOT("probably_not"),
    NO("no");

    fun likelihood(hasAttribute: Boolean): Double {
        return when (this) {
            YES -> if (hasAttribute) 1.0 else 0.05
            PROBABLY -> if (hasAttribute) 0.85 else 0.15
            SOMETIMES -> 0.55
            PROBABLY_NOT -> if (hasAttribute) 0.15 else 0.85
            NO -> if (hasAttribute) 0.05 else 1.0
        }
    }

    fun contradicts(hasAttribute: Boolean): Boolean {
        return when (this) {
            YES, PROBABLY -> !hasAttribute
            NO, PROBABLY_NOT -> hasAttribute
            SOMETIMES -> false
        }
    }

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

data class MindReaderCandidateScore(
    val entity: MindReaderEntity,
    val weight: Double
)

data class MindReaderPopQuizChoice(
    val entity: MindReaderEntity
)

data class MindReaderPopQuizQuestion(
    val prompt: LocalizedText,
    val correctEntity: MindReaderEntity,
    val choices: List<MindReaderPopQuizChoice>
) {
    fun isCorrect(choice: MindReaderPopQuizChoice): Boolean {
        return choice.entity.id == correctEntity.id
    }
}

data class MindReaderContradictionDetail(
    val attributeId: String,
    val question: LocalizedText,
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
    val confidence: Double
)

data class MindReaderHistoryEntry(
    val attributeId: String,
    val question: LocalizedText,
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
    val attributeId: String, // Kept for history tracking, can be a random UUID
    val question: LocalizedText
)

data class MindReaderGameState(
    val worldKey: String? = null,
    val candidates: List<MindReaderCandidateScore> = emptyList(), // Optional, AI might not use it
    val askedAttributes: Set<String> = emptySet(),
    val questionCount: Int = 0,
    val history: MindReaderGameHistory = MindReaderGameHistory(),
    val pendingGuess: MindReaderGuessResult? = null // Added to store AI's guess
)

data class MindReaderDataset(
    val attributes: List<MindReaderAttribute>,
    val entities: List<MindReaderEntity>,
    val config: MindReaderGameConfig
)

data class MindReaderGameLaunch(
    val languageCode: String,
    val nativeLanguageCode: String = "en",
    val dataset: MindReaderDataset,
    val state: MindReaderGameState
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
        val reason: String? = null
    ) : MindReaderResult

    data class Timeout(
        val guess: MindReaderGuessResult?,
        val history: MindReaderGameHistory
    ) : MindReaderResult
}
