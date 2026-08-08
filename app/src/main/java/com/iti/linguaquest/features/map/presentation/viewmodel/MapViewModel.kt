package com.iti.linguaquest.features.map.presentation.viewmodel

import com.iti.linguaquest.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            MapIntent.Retry -> loadLevels(_state.value.worldId, force = true)
        }
    }

    fun loadLevels(worldId: Int, force: Boolean = false) {
        if (!force && _state.value.levels.isNotEmpty() && _state.value.worldId == worldId && !_state.value.hasError) return

        viewModelScope.launch {
            if (_state.value.levels.isEmpty()) {
                _state.update { it.copy(isLoading = true, worldId = worldId, hasError = false, errorMessage = null) }
            } else {
                _state.update { it.copy(worldId = worldId, hasError = false, errorMessage = null) }
            }
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
                        levelId = level.id,
                        targetWord = level.word
                    )
                }

                var currentIndex = uiLevels.indexOfFirst { it.status == LevelStatus.CURRENT }
                if (currentIndex == -1 && uiLevels.isNotEmpty()) {
                    currentIndex = uiLevels.lastIndex
                }
                
                _state.update {
                    it.copy(
                        isLoading = false,
                        worldTitle = UiText.DynamicString(detail.name),
                        levels = uiLevels,
                        currentLevelIndex = currentIndex,
                        hasError = false,
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                val uiText = (error as? LinguaQuestDataError)?.toUiText()
                    ?: UiText.StringResource(R.string.general_error)

                _state.update {
                    it.copy(
                        isLoading = false,
                        hasError = true,
                        errorMessage = uiText.toString()
                    )
                }

                snackbarController.sendEvent(
                    SnackbarEvent(
                        message = uiText,
                        type = SnackbarType.ERROR,
                        actionLabel = UiText.StringResource(R.string.retry),
                        onAction = { loadLevels(worldId, force = true) }
                    )
                )
            }
        }
    }

    private fun handleLevelClicked(levelId: Int) {
        val level = _state.value.levels.find { it.levelId == levelId } ?: return
        if (level.status == LevelStatus.LOCKED) return
        sendEffect(MapEffect.NavigateToLevel(levelId, level.levelNumber, level.targetWord))
    }

    private fun sendEffect(effect: MapEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}
