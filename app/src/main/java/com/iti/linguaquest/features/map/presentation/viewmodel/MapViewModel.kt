package com.iti.linguaquest.features.map.presentation.viewmodel

import com.iti.linguaquest.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.connectivity.NetworkMonitor
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
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
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.onSuccess
import com.iti.linguaquest.core.result.onFailure
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getMapLevelsUseCase: GetMapLevelsUseCase,
    private val snackbarController: SnackbarController,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )


    private val _state = MutableStateFlow(MapState())
    val state = _state.asStateFlow()

    private val _effects = Channel<MapEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: MapIntent) {
        when (intent) {
            is MapIntent.LevelClicked -> handleLevelClicked(intent.levelId)
            MapIntent.BackClicked -> sendEffect(MapEffect.NavigateBack)
        }
    }

    fun loadLevels(worldId: Int) {
        if (_state.value.levels.isNotEmpty() && _state.value.worldId == worldId) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, worldId = worldId) }
            val result = getMapLevelsUseCase(worldId)
            
            result.onSuccess { detail ->
                val uiLevels = detail.levels.map { level ->
                    val status = when (level.status) {
                        "AVAILABLE", "INPROGRESS" -> LevelStatus.CURRENT
                        "COMPLETED" -> LevelStatus.COMPLETED
                        else -> LevelStatus.LOCKED
                    }
                    MapLevelUiModel(
                        levelNumber = level.order,
                        status = status,
                        stars = if (status == LevelStatus.COMPLETED) 3 else 0,
                        levelId = level.id
                    )
                }

                val currentIndex = uiLevels.indexOfFirst { it.status == LevelStatus.CURRENT }
                _state.update {
                    it.copy(
                        isLoading = false,
                        worldTitle = UiText.DynamicString(detail.name),
                        levels = uiLevels,
                        currentLevelIndex = currentIndex
                    )
                }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false) }
                val uiText = (error as? LinguaQuestDataError)?.toUiText()
                    ?: UiText.StringResource(R.string.general_error)
                
                snackbarController.sendEvent(
                    SnackbarEvent(
                        message = uiText,
                        type = SnackbarType.ERROR,
                        actionLabel = UiText.StringResource(R.string.retry),
                        onAction = { loadLevels(worldId) }
                    )
                )
            }
        }
    }

    private fun handleLevelClicked(levelId: Int) {
        val level = _state.value.levels.find { it.levelId == levelId } ?: return
        if (level.status == LevelStatus.LOCKED) return
        sendEffect(MapEffect.NavigateToLevel(levelId))
    }

    private fun sendEffect(effect: MapEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}
