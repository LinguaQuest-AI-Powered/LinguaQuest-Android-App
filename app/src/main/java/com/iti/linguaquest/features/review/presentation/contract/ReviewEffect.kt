package com.iti.linguaquest.features.review.presentation.contract

sealed interface ReviewEffect {
    data object NavigateBack : ReviewEffect
    data class SpeakText(
        val text: String,
        val language: String,
        val sectionId: String? = null
    ) : ReviewEffect

    data object StopSpeaking : ReviewEffect
}
