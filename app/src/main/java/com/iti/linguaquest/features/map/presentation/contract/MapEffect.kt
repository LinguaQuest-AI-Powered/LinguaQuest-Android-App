package com.iti.linguaquest.features.map.presentation.contract

sealed interface MapEffect {
    data object NavigateBack : MapEffect
    data class NavigateToLevel(val levelId: Int) : MapEffect
}
