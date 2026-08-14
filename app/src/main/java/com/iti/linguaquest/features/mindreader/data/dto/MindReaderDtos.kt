package com.iti.linguaquest.features.mindreader.data.dto

data class CategoryDto(
    val id: String,
    val displayName: String,
    val emoji: String = ""
)

data class MindReaderNextTurnDto(
    val type: String,
    val questionTargetText: String?,
    val questionNativeText: String?,
    val guessWord: String?,
    val guessTranslation: String?,
    val guessEmoji: String?
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
