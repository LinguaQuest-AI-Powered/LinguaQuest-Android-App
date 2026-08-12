package com.iti.linguaquest.core.tutorial.model

data class TutorialStep(
    val stepId: String,
    val titleRes: Int,
    val descriptionRes: Int,
    val lingoImageRes: Int
)

data class TutorialTour(
    val tourId: String,
    val steps: List<TutorialStep>
)
