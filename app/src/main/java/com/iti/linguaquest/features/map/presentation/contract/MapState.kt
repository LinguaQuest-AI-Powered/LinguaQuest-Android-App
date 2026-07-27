package com.iti.linguaquest.features.map.presentation.contract

import com.iti.linguaquest.features.map.presentation.components.LevelStatus
import com.iti.linguaquest.core.sharedComponents.text.UiText

data class MapLevelUiModel(
    val levelNumber: Int,
    val status: LevelStatus,
    val stars: Int,
    val levelId: Int = 0
)

data class MapState(
    val isLoading: Boolean = false,
    val worldId: Int = 0,
    val worldTitle: UiText = UiText.DynamicString("Park World"),
    val levels: List<MapLevelUiModel> = emptyList(),
    val currentLevelIndex: Int = -1,
    val hasError: Boolean = false,
    val errorMessage: String? = null
)
