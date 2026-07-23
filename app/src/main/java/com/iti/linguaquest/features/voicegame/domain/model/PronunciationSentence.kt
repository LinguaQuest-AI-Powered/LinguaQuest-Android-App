package com.iti.linguaquest.features.voicegame.domain.model

data class PronunciationSentence(
    val sentence: String,
    val difficulty: String = "Beginner",
    val phonetic: String? = null,
    val translation: String? = null
)
