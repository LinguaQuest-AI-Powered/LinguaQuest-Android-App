package com.iti.linguaquest.features.home.presentation.contract

sealed interface HomeEffect {
    data class NavigateToLessonDetails(val lessonId: Int) : HomeEffect
    data class NavigateToWorld(val worldId: String) : HomeEffect
    data object NavigateToAllWorlds : HomeEffect
}