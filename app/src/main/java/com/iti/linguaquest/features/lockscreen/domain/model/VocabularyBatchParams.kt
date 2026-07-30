package com.iti.linguaquest.features.lockscreen.domain.model

data class VocabularyBatchParams(
    val batchSize: Int,
    val excludeWords: List<String>,
    val nativeLanguage: String,
    val targetLanguage: String,
    val proficiencyLevel: String
)
