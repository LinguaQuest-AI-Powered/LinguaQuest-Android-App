package com.iti.linguaquest.features.game.presentation.result.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.appicon.usecase.LessonCompletedUseCase
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.core.wallet.domain.usecase.RefreshWalletUseCase
import com.iti.linguaquest.features.game.domain.usecase.GetHintUseCase
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultEffect
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultIntent
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GameResultViewModel @Inject constructor(
    private val lessonCompletedUseCase: LessonCompletedUseCase,
    private val getHintUseCase: GetHintUseCase,
    private val refreshWalletUseCase: RefreshWalletUseCase,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _state = MutableStateFlow<GameResultUiState>(GameResultUiState.Error(UiText.StringResource(R.string.game_result_loading)))
    val state: StateFlow<GameResultUiState> = _state.asStateFlow()

    private val _effect = Channel<GameResultEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: GameResultIntent) {
        when (intent) {
            GameResultIntent.RetryClicked -> sendEffect(GameResultEffect.NavigateToCamera)
            is GameResultIntent.BuyHintClicked -> buyHint(intent.worldId, intent.levelId)
            GameResultIntent.NextLevelClicked -> sendEffect(GameResultEffect.NavigateToNextLevel)
            GameResultIntent.ExitClicked -> sendEffect(GameResultEffect.NavigateToExit)
        }
    }

    fun setInitialResult(resultState: GameResultUiState) {
        _state.value = resultState
        if (resultState is GameResultUiState.Success) {
            viewModelScope.launch {
                lessonCompletedUseCase()
            }
        }
    }

    private fun sendEffect(effect: GameResultEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    private fun buyHint(worldId: Int, levelId: Int) {
        viewModelScope.launch {
            when (val result = getHintUseCase(worldId, levelId)) {
                is LinguaQuestResult.Success -> {
                    sendEffect(GameResultEffect.ApplyHintAndRetry(result.data.hint))
                    refreshWalletUseCase()
                }
                is LinguaQuestResult.Failure -> {
                    val errorUiText = (result.error as? LinguaQuestDataError)?.toUiText()
                        ?: UiText.StringResource(R.string.error_generic)
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = errorUiText,
                            type = SnackbarType.ERROR
                        )
                    )
                }
            }
        }
    }
}
