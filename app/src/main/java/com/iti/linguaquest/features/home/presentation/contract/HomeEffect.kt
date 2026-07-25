package com.iti.linguaquest.features.home.presentation.contract

sealed interface HomeEffect {
    data object NavigateToVoiceGame : HomeEffect
    data class NavigateToWorld(val worldId: Int) : HomeEffect
    data object NavigateToAllWorlds : HomeEffect
    data object NavigateToAddLanguages : HomeEffect
}