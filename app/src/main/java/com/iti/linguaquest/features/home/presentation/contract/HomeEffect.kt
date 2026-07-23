package com.iti.linguaquest.features.home.presentation.contract

sealed interface HomeEffect {
    data class NavigateToVoiceGame(val lessonId: Int, val sentence: String) : HomeEffect
    data class NavigateToWorld(val worldId: Int) : HomeEffect
    data object NavigateToAllWorlds : HomeEffect
    data object NavigateToAddLanguages : HomeEffect
}