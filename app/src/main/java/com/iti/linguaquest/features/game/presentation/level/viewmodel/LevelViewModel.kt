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
import kotlinx.coroutines.flow.firstOrNull
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
import com.iti.linguaquest.core.wallet.domain.usecase.GetWalletUseCase
import com.iti.linguaquest.core.wallet.domain.usecase.RefreshWalletUseCase
import com.iti.linguaquest.features.game.domain.usecase.ChangeWordUseCase
import com.iti.linguaquest.features.game.domain.usecase.GetHintUseCase
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import com.iti.linguaquest.core.domain.model.GameCost
import com.iti.linguaquest.features.game.domain.usecase.StartLevelUseCase

@HiltViewModel
class LevelViewModel @Inject constructor(
    private val startLevelUseCase: StartLevelUseCase,
    private val changeWordUseCase: ChangeWordUseCase,
    private val getHintUseCase: GetHintUseCase,
    private val refreshWalletUseCase: RefreshWalletUseCase,
    private val getWalletUseCase: GetWalletUseCase,
    private val snackbarController: SnackbarController,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    private val userPreferencesRepository: com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(LevelState())
    val state = _state.asStateFlow()

    private val _effects = Channel<LevelEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    init {
        viewModelScope.launch {
            getWalletUseCase().collect { wallet ->
                _state.update { it.copy(coinCount = wallet.coins) }
            }
        }
    }

    fun loadLevelDetails(worldId: Int, levelId: Int, levelOrder: Int, targetWord: String? = null) {
        if (_state.value.worldId == worldId && _state.value.levelId == levelId && _state.value.isLevelReady) {
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    worldId = worldId,
                    levelId = levelId,
                    levelOrder = levelOrder,
                    isLoading = true,
                    isHintLoading = false
                )
            }

            if (!targetWord.isNullOrEmpty()) {
                _state.update {
                    it.copy(
                        wordToGuess = targetWord,
                        isLoading = false,
                        isLevelReady = true,
                        isChangeWordAvailable = true
                    )
                }
                return@launch
            }

            // _state.update { it.copy(isChangeWordAvailable = false) }

            refreshWalletUseCase()
            _state.update {
                it.copy(
                    isLoading = true,
                    worldId = worldId,
                    levelId = levelId,
                    levelOrder = levelOrder,
                    isLevelReady = false,
                    isChangeWordAvailable = true,
                    isChangeWordDialogVisible = false,
                    isChangeWordUsed = false
                )
            }
            when (val result = startLevelUseCase(worldId, levelId)) {
                is LinguaQuestResult.Success -> {
                    val targetWordResult = result.data.ifEmpty { if (worldId == 1 && levelOrder == 3) "PAN" else "APPLE" }
                    val languageCode = userPreferencesRepository.targetLanguageCode.firstOrNull() ?: "en"

                    _state.update {
                        it.copy(
                            isLoading = false,
                            worldId = worldId,
                            levelId = levelId,
                            levelOrder = levelOrder,
                            wordToGuess = targetWordResult,
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
            LevelIntent.RetryClicked -> loadLevelDetails(_state.value.worldId, _state.value.levelId, _state.value.levelOrder)
        }
    }

    private fun skipCurrentWord() {
        changeCurrentWord(cost = 0, markAsUsed = false)
    }

    private fun changeCurrentWord(cost: Int, markAsUsed: Boolean) {
        val worldId = _state.value.worldId
        val levelId = _state.value.levelId

        viewModelScope.launch {
            if (cost > 0) {
                _state.update { it.copy(coinCount = maxOf(0, it.coinCount - cost)) }
            }
            _state.update { it.copy(isLoading = true) }
            when (val result = changeWordUseCase(worldId, levelId)) {
                is LinguaQuestResult.Success -> {
                    val newWord = result.data.ifEmpty { "Platano" }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            wordToGuess = newWord,
                            isChangeWordUsed = markAsUsed,
                            isChangeWordAvailable = !markAsUsed
                        )
                    }
                    refreshWalletUseCase()
                }
                is LinguaQuestResult.Failure -> {
                    refreshWalletUseCase()
                    val shouldDisableChangeWord = isInsufficientCoinsError(result.error)
                    _state.update {
                        it.copy(
                            coinCount = if (cost > 0) it.coinCount + cost else it.coinCount,
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
        if (current.isLoading || !current.isLevelReady) {
            return
        }
        _state.update { it.copy(isChangeWordDialogVisible = true) }
    }

    private fun confirmChangeWord() {
        val current = _state.value
        if (current.isLoading || !current.isLevelReady || current.coinCount < GameCost.CHANGE_WORD.coins) {
            _state.update { it.copy(isChangeWordDialogVisible = false) }
            return
        }

        _state.update { it.copy(isChangeWordDialogVisible = false) }
        changeCurrentWord(cost = GameCost.CHANGE_WORD.coins, markAsUsed = true)
    }

    private fun buyHint() {
        val worldId = _state.value.worldId
        val levelId = _state.value.levelId

        viewModelScope.launch {
            _state.update { it.copy(coinCount = maxOf(0, it.coinCount - GameCost.HINT.coins), isHintLoading = true) }
            when (val result = getHintUseCase(worldId, levelId)) {
                is LinguaQuestResult.Success -> {
                    _state.update {
                        it.copy(
                            isHintLoading = false,
                            isBottomSheetVisible = false,
                            hintText = result.data.hint,
                            coinCount = result.data.remainingCoins
                        )
                    }
                    sendEffect(LevelEffect.HintRetrieved(result.data.hint))
                    refreshWalletUseCase()
                }
                is LinguaQuestResult.Failure -> {
                    refreshWalletUseCase()
                    _state.update { it.copy(isHintLoading = false, coinCount = it.coinCount + GameCost.HINT.coins, isBottomSheetVisible = false) }
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
