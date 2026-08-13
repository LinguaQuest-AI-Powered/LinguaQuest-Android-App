package com.iti.linguaquest.core.tutorial.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class TutorialStep(
    val stepId: String,
    @param:StringRes val titleRes: Int,
    @param:StringRes val descriptionRes: Int,
    @param:DrawableRes val lingoImageRes: Int
)

data class TutorialTour(
    val tourId: TourId,
    val steps: List<TutorialStep>
)
