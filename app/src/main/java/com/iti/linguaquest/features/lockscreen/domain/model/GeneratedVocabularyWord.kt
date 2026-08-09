package com.iti.linguaquest.features.lockscreen.domain.model

data class GeneratedVocabularyWord(
    val word: String,
    val translation: String,
    val exampleSentence: String,
    val difficulty: String = "Medium",
    val meaning: String = ""
)
