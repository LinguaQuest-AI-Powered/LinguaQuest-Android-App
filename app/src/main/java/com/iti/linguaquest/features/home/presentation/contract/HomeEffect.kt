package com.iti.linguaquest.features.home.presentation.contract

sealed interface HomeEffect {
    data class NavigateToWorld(val worldId: Int) : HomeEffect
    data object NavigateToAllWorlds : HomeEffect
    data object NavigateToAddLanguages : HomeEffect
    data class NavigateToContinueLevel(val worldId: Int, val levelId: Int, val levelOrder: Int) : HomeEffect
}