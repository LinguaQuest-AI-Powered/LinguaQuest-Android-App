package com.iti.linguaquest.features.game.presentation.level.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.game.presentation.level.contract.LevelEffect
import com.iti.linguaquest.features.game.presentation.level.contract.LevelIntent
import com.iti.linguaquest.features.game.presentation.level.contract.LevelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.R
import com.iti.linguaquest.features.game.domain.usecase.ChangeWordUseCase
import com.iti.linguaquest.features.game.domain.usecase.StartLevelUseCase

@HiltViewModel
class LevelViewModel @Inject constructor(
    private val startLevelUseCase: StartLevelUseCase,
    private val changeWordUseCase: ChangeWordUseCase,
    private val snackbarController: SnackbarController,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(LevelState())
    val state = _state.asStateFlow()

    private val _effects = Channel<LevelEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun loadLevelDetails(worldId: Int, levelNumber: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, worldId = worldId, levelNumber = levelNumber, hasError = false, errorMessage = null) }
            when (val result = startLevelUseCase(worldId, levelNumber)) {
                is LinguaQuestResult.Success -> {
                    val targetWord = result.data.ifEmpty { if (worldId == 1 && levelNumber == 3) "PAN" else "APPLE" }
                    val coinCount = 1250
                    val languageCode = if (worldId == 1) "es" else "en"

                    _state.update {
                        it.copy(
                            isLoading = false,
                            worldId = worldId,
                            levelNumber = levelNumber,
                            wordToGuess = targetWord,
                            coinCount = coinCount,
                            languageCode = languageCode,
                            hasError = false,
                            errorMessage = null
                        )
                    }
                }
                is LinguaQuestResult.Failure -> {
                    val uiText = (result.error as? LinguaQuestDataError)?.toUiText()
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
                            type = SnackbarType.ERROR
                        )
                    )
                }
            }
        }
    }

    fun onIntent(intent: LevelIntent) {
        when (intent) {
            LevelIntent.BackClicked -> sendEffect(LevelEffect.NavigateBack)
            LevelIntent.DismissBottomSheet -> _state.update { it.copy(isBottomSheetVisible = false) }
            LevelIntent.MascotTapped -> _state.update { it.copy(isBottomSheetVisible = true) }
            LevelIntent.OpenCameraClicked -> sendEffect(LevelEffect.LaunchCamera)
            LevelIntent.SkipClicked -> skipCurrentWord()
            LevelIntent.RevealFirstLetterClicked -> deductCoinsAndHideSheet(25)
            LevelIntent.ShowCategoryClueClicked -> deductCoinsAndHideSheet(50)
            LevelIntent.SoundClicked -> sendEffect(
                LevelEffect.PlaySound(
                    word = _state.value.wordToGuess,
                    languageCode = _state.value.languageCode
                )
            )
            LevelIntent.RetryClicked -> loadLevelDetails(_state.value.worldId, _state.value.levelNumber)
        }
    }

    private fun skipCurrentWord() {
        val worldId = _state.value.worldId
        val levelNumber = _state.value.levelNumber

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = changeWordUseCase(worldId, levelNumber)) {
                is LinguaQuestResult.Success -> {
                    val newWord = result.data.ifEmpty { "Platano" }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            wordToGuess = newWord
                        )
                    }
                }
                is LinguaQuestResult.Failure -> {
                    _state.update { it.copy(isLoading = false) }
                    val uiText = (result.error as? LinguaQuestDataError)?.toUiText()
                        ?: UiText.StringResource(R.string.general_error)

                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = uiText,
                            type = SnackbarType.ERROR
                        )
                    )
                }
            }
        }
    }

    private fun deductCoinsAndHideSheet(cost: Int) {
        _state.update {
            val newCoins = if (it.coinCount >= cost) it.coinCount - cost else it.coinCount
            it.copy(
                coinCount = newCoins,
                isBottomSheetVisible = false
            )
        }
    }

    private fun sendEffect(effect: LevelEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}
