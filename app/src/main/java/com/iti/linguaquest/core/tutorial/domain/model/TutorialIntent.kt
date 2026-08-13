package com.iti.linguaquest.core.tutorial.domain.model

import com.iti.linguaquest.core.tutorial.model.TargetBounds
import com.iti.linguaquest.core.tutorial.model.TutorialTour

sealed interface TutorialIntent {
    data class StartAppTour(val force: Boolean = false) : TutorialIntent
    data class StartGalleryTour(val force: Boolean = false) : TutorialIntent
    data class StartLingosTour(val force: Boolean = false) : TutorialIntent
    
    data class StartProfileTour(
        val force: Boolean = false,
        val hasAchievements: Boolean = true,
        val hasLeaderboard: Boolean = true
    ) : TutorialIntent

    data class StartTour(val tour: TutorialTour, val force: Boolean = false) : TutorialIntent
    data class RegisterTarget(val stepId: String, val bounds: TargetBounds) : TutorialIntent
    data class UnregisterTarget(val stepId: String) : TutorialIntent
    data object NextStep : TutorialIntent
    data object SkipTour : TutorialIntent
    data object ResetAllTours : TutorialIntent
}
