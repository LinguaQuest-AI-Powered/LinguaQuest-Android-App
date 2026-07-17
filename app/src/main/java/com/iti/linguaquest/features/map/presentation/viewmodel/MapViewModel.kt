package com.iti.linguaquest.features.map.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.map.domain.usecase.GetMapLevelsUseCase
import com.iti.linguaquest.features.map.presentation.components.LevelStatus
import com.iti.linguaquest.features.map.presentation.contract.MapEffect
import com.iti.linguaquest.features.map.presentation.contract.MapIntent
import com.iti.linguaquest.features.map.presentation.contract.MapLevelUiModel
import com.iti.linguaquest.features.map.presentation.contract.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getMapLevelsUseCase: GetMapLevelsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MapState())
    val state = _state.asStateFlow()

    private val _effects = Channel<MapEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: MapIntent) {
        when (intent) {
            is MapIntent.LevelClicked -> handleLevelClicked(intent.levelNumber)
            MapIntent.BackClicked -> sendEffect(MapEffect.NavigateBack)
        }
    }

    fun loadLevels(totalLevels: Int, completedLevels: Int) {
        if (_state.value.levels.isNotEmpty()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val domainLevels = getMapLevelsUseCase(totalLevels, completedLevels)
            
            val uiLevels = domainLevels.map { level ->
                val status = when {
                    level.isCurrent -> LevelStatus.CURRENT
                    level.isCompleted -> LevelStatus.COMPLETED
                    else -> LevelStatus.LOCKED
                }
                MapLevelUiModel(
                    levelNumber = level.levelNumber,
                    status = status,
                    stars = level.stars
                )
            }

            val currentIndex = uiLevels.indexOfFirst { it.status == LevelStatus.CURRENT }
            _state.update {
                it.copy(
                    isLoading = false,
                    levels = uiLevels,
                    currentLevelIndex = currentIndex
                )
            }
        }
    }

    private fun handleLevelClicked(levelNumber: Int) {
        val level = _state.value.levels.find { it.levelNumber == levelNumber } ?: return
        if (level.status == LevelStatus.LOCKED) return
        sendEffect(MapEffect.NavigateToLevel(levelNumber))
    }

    private fun sendEffect(effect: MapEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}
