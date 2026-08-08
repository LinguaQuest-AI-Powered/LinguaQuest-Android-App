package com.iti.linguaquest.features.mindreader.data.datasource.remote.mdoel

data class MindReaderNextStepResponse(
    val type: String,
    val questionTargetText: String?,
    val questionNativeText: String?,
    val guessWord: String?,
    val guessTranslation: String?,
    val guessEmoji: String?
)

data class MindReaderQuizChoice(
    val translationText: String,
    val isCorrect: Boolean
)

data class MindReaderQuizResponse(
    val choices: List<MindReaderQuizChoice>
)

data class MindReaderHonestyResponse(
    val isHonest: Boolean,
    val explanation: String
)