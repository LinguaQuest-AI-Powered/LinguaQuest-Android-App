package com.iti.linguaquest.features.review.presentation.contract

import com.iti.linguaquest.core.database.word.WordEntity

sealed interface ReviewIntent {
    data class LoadReview(val word: WordEntity) : ReviewIntent
    data object RetryClicked : ReviewIntent
    data object BackClicked : ReviewIntent
    data class SpeakSection(
        val text: String,
        val language: String,
        val sectionId: String
    ) : ReviewIntent
}
