package com.iti.linguaquest.features.review.domain.model
data class AIReviewResponse(
    val exampleSentence: String,
    val sentenceTranslation: String,
    val memoryTip: String,
    val funFact: String,
    val fullText: String
)