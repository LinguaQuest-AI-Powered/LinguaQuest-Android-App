package com.iti.linguaquest.features.game.presentation.processing.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.features.game.presentation.processing.contract.GameProcessingEffect
import com.iti.linguaquest.features.game.presentation.processing.contract.GameProcessingIntent
import com.iti.linguaquest.features.game.presentation.processing.contract.GameProcessingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.game.domain.usecase.VerifyLevelUseCase
import java.io.File

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText

@HiltViewModel
class GameProcessingViewModel @Inject constructor(
    private val verifyLevelUseCase: VerifyLevelUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameProcessingState())
    val state: StateFlow<GameProcessingState> = _state.asStateFlow()

    private val _effect = Channel<GameProcessingEffect>()
    val effect = _effect.receiveAsFlow()

    fun verifyImage(worldId: Int, levelId: Int, imageFile: File?) {
        if (imageFile == null || !imageFile.exists()) {
            sendEffect(GameProcessingEffect.NavigateToError(UiText.StringResource(R.string.game_processing_simulate_error_message)))
            return
        }
        viewModelScope.launch {
            when (val result = verifyLevelUseCase(worldId, levelId, imageFile)) {
                is LinguaQuestResult.Success -> {
                    val data = result.data
                    if (data.isMatch) {
                        sendEffect(
                            GameProcessingEffect.NavigateToSuccess(
                                xp = data.xpEarned,
                                coins = data.coinsEarned,
                                level = data.level,
                                progressPercentage = data.levelProgressPercentage
                            )
                        )
                    } else {
                        sendEffect(
                            GameProcessingEffect.NavigateToFailure(
                                reason = UiText.StringResource(R.string.game_processing_simulate_failure_reason)
                            )
                        )
                    }
                }
                is LinguaQuestResult.Failure -> {
                    val uiText = (result.error as? LinguaQuestDataError)?.toUiText()
                        ?: UiText.StringResource(R.string.general_error)
                    sendEffect(GameProcessingEffect.NavigateToError(errorMessage = uiText))
                }
            }
        }
    }
    fun onIntent(intent: GameProcessingIntent) {
        when (intent) {
            GameProcessingIntent.StartProcessing -> {}
        }
    }

    private fun sendEffect(effect: GameProcessingEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}