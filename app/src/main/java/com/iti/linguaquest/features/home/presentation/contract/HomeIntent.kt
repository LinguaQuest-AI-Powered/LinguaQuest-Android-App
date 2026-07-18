package com.iti.linguaquest.features.home.presentation.contract

import com.iti.linguaquest.features.home.presentation.view.components.WorldItem

sealed interface HomeIntent {
    data object LoadHome : HomeIntent
    data object Retry : HomeIntent
    data class WorldClicked(val world: WorldItem) : HomeIntent
    data object ContinueLessonClicked : HomeIntent
    data object SeeMoreWorldsClicked : HomeIntent
}