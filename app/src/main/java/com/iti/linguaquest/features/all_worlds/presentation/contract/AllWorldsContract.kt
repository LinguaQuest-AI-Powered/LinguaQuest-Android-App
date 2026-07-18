package com.iti.linguaquest.features.all_worlds.presentation.contract

import com.iti.linguaquest.features.home.presentation.view.components.WorldDifficulty
import com.iti.linguaquest.features.home.presentation.view.components.WorldItem

data class AllWorldsState(
    val isLoading: Boolean = false,
    val selectedFilter: WorldDifficulty? = null, // null means "All"
    val worlds: List<WorldItem> = emptyList()
) {
    val filteredWorlds: List<WorldItem>
        get() = if (selectedFilter == null) {
            worlds
        } else {
            worlds.filter { it.difficulty == selectedFilter }
        }
}

sealed interface AllWorldsIntent {
    data class OnFilterSelected(val filter: WorldDifficulty?) : AllWorldsIntent
    data class OnWorldClicked(val world: WorldItem) : AllWorldsIntent
    data object OnBackClicked : AllWorldsIntent
}

sealed interface AllWorldsEffect {
    data class NavigateToWorldDetails(val worldId: String) : AllWorldsEffect
    data object NavigateBack : AllWorldsEffect
}
