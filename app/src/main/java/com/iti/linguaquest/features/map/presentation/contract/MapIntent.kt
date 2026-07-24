package com.iti.linguaquest.features.map.presentation.contract

sealed interface MapIntent {
    data class LevelClicked(val levelNumber: Int) : MapIntent
    data object BackClicked : MapIntent
    data object Retry : MapIntent
}
