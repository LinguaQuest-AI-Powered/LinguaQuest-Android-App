package com.iti.linguaquest.features.review.presentation.contract

import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.features.review.domain.model.AIReviewResponse

import com.iti.linguaquest.core.sharedComponents.text.UiText

data class ReviewState(
    val word: WordEntity? = null,
    val isLoading: Boolean = false,
    val aiResponse: AIReviewResponse? = null,
    val errorMessage: UiText? = null,
    val isSpeaking: Boolean = false,
    val speakingSectionId: String? = null
)

