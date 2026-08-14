package com.iti.linguaquest.features.mindreader.data.dto

data class CategoryDto(
    val id: String,
    val displayName: String,
    val displayNames: Map<String, String>? = null,
    val emoji: String = "",
    val seedQuestions: List<Map<String, String>> = emptyList()
)

data class MindReaderNextTurnDto(
    val type: String,
    val questionTargetText: String?,
    val questionNativeText: String?,
    val guessWord: String?,
    val guessTranslation: String?,
    val guessEmoji: String?,
    val quizChoices: List<MindReaderQuizChoiceDto>? = null
)

data class MindReaderQuizChoiceDto(
    val translationText: String,
    val isCorrect: Boolean
)

data class MindReaderQuizDto(
    val choices: List<MindReaderQuizChoiceDto>
)

data class MindReaderHonestyDto(
    val isHonest: Boolean,
    val explanation: String
)
