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
import com.iti.linguaquest.core.result.AppError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.R
import com.iti.linguaquest.core.wallet.domain.usecase.RefreshWalletUseCase
import com.iti.linguaquest.features.game.domain.usecase.ChangeWordUseCase
import com.iti.linguaquest.features.game.domain.usecase.GetHintUseCase
import com.iti.linguaquest.features.game.domain.usecase.StartLevelUseCase

@HiltViewModel
class LevelViewModel @Inject constructor(
    private val startLevelUseCase: StartLevelUseCase,
    private val changeWordUseCase: ChangeWordUseCase,
    private val getHintUseCase: GetHintUseCase,
    private val refreshWalletUseCase: RefreshWalletUseCase,
    private val snackbarController: SnackbarController,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(LevelState())
    val state = _state.asStateFlow()

    private val _effects = Channel<LevelEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun loadLevelDetails(worldId: Int, levelNumber: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    worldId = worldId,
                    levelNumber = levelNumber,
                    isLevelReady = false,
                    isChangeWordAvailable = false,
                    isChangeWordDialogVisible = false,
                    isChangeWordUsed = false
                )
            }
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
                            isLevelReady = true,
                            isChangeWordAvailable = true,
                            isChangeWordDialogVisible = false
                        )
                    }
                }
                is LinguaQuestResult.Failure -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isLevelReady = false,
                            isChangeWordAvailable = false
                        )
                    }
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

    fun onIntent(intent: LevelIntent) {
        when (intent) {
            LevelIntent.BackClicked -> sendEffect(LevelEffect.NavigateBack)
            LevelIntent.DismissBottomSheet -> _state.update { it.copy(isBottomSheetVisible = false) }
            LevelIntent.MascotTapped -> _state.update { it.copy(isBottomSheetVisible = true) }
            LevelIntent.OpenCameraClicked -> {
                if (_state.value.isLevelReady && !_state.value.isLoading) {
                    sendEffect(LevelEffect.LaunchCamera)
                }
            }
            LevelIntent.ChangeWordClicked -> openChangeWordDialog()
            LevelIntent.ConfirmChangeWordClicked -> confirmChangeWord()
            LevelIntent.CancelChangeWordClicked -> _state.update { it.copy(isChangeWordDialogVisible = false) }
            LevelIntent.SkipClicked -> skipCurrentWord()
            LevelIntent.GetHintClicked -> buyHint()
            LevelIntent.SoundClicked -> sendEffect(
                LevelEffect.PlaySound(
                    word = _state.value.wordToGuess,
                    languageCode = _state.value.languageCode
                )
            )
        }
    }

    private fun skipCurrentWord() {
        changeCurrentWord(cost = 0, markAsUsed = false)
    }

    private fun changeCurrentWord(cost: Int, markAsUsed: Boolean) {
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
                            wordToGuess = newWord,
                            coinCount = if (cost > 0 && it.coinCount >= cost) it.coinCount - cost else it.coinCount,
                            isChangeWordUsed = markAsUsed,
                            isChangeWordAvailable = if (markAsUsed) false else it.isChangeWordAvailable
                        )
                    }
                }
                is LinguaQuestResult.Failure -> {
                    val shouldDisableChangeWord = isInsufficientCoinsError(result.error)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isChangeWordAvailable = if (shouldDisableChangeWord) false else it.isChangeWordAvailable
                        )
                    }
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

    private fun openChangeWordDialog() {
        val current = _state.value
        if (current.isLoading || !current.isLevelReady || current.isChangeWordUsed || current.coinCount < 50) {
            return
        }
        _state.update { it.copy(isChangeWordDialogVisible = true) }
    }

    private fun confirmChangeWord() {
        val current = _state.value
        if (current.isLoading || !current.isLevelReady || current.isChangeWordUsed || current.coinCount < 50) {
            _state.update { it.copy(isChangeWordDialogVisible = false) }
            return
        }

        _state.update { it.copy(isChangeWordDialogVisible = false) }
        changeCurrentWord(cost = 50, markAsUsed = true)
    }

    private fun buyHint() {
        val worldId = _state.value.worldId
        val levelNumber = _state.value.levelNumber

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getHintUseCase(worldId, levelNumber)) {
                is LinguaQuestResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isBottomSheetVisible = false,
                            hintText = result.data.hint,
                            coinCount = result.data.remainingCoins
                        )
                    }
                    sendEffect(LevelEffect.HintRetrieved(result.data.hint))
                    refreshWalletUseCase()
                }
                is LinguaQuestResult.Failure -> {
                    _state.update { it.copy(isLoading = false, isBottomSheetVisible = false) }
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

    private fun sendEffect(effect: LevelEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }

    private fun isInsufficientCoinsError(error: AppError): Boolean {
        val message = (error as? LinguaQuestDataError.CustomServerMessage)?.message.orEmpty()
        val normalized = message.lowercase()
        return normalized.contains("insufficient") && normalized.contains("coin")
    }
}
