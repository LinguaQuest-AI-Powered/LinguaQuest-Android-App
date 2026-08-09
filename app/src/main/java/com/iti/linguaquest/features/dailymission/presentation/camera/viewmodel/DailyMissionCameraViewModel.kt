package com.iti.linguaquest.features.dailymission.presentation.camera.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.dailymission.domain.usecase.VerifyDailyMissionUseCase
import com.iti.linguaquest.features.dailymission.presentation.camera.contract.DailyMissionCameraEffect
import com.iti.linguaquest.features.dailymission.presentation.camera.contract.DailyMissionCameraIntent
import com.iti.linguaquest.features.dailymission.presentation.camera.contract.DailyMissionCameraState
import com.iti.linguaquest.features.game.presentation.camera.contract.PermissionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyMissionCameraViewModel @Inject constructor(
    private val verifyDailyMissionUseCase: VerifyDailyMissionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DailyMissionCameraState())
    val state: StateFlow<DailyMissionCameraState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DailyMissionCameraEffect>()
    val effect: SharedFlow<DailyMissionCameraEffect> = _effect.asSharedFlow()

    fun onIntent(intent: DailyMissionCameraIntent) {
        when (intent) {
            is DailyMissionCameraIntent.InitWord -> _state.update { it.copy(word = intent.word) }
            DailyMissionCameraIntent.BackClicked -> sendEffect(DailyMissionCameraEffect.NavigateBack)
            is DailyMissionCameraIntent.CapturePhoto -> _state.update { it.copy(capturedUri = intent.uri) }
            DailyMissionCameraIntent.GrantPermissionClicked -> {}
            is DailyMissionCameraIntent.PermissionResult -> {
                _state.update {
                    it.copy(permissionStatus = if (intent.isGranted) PermissionStatus.GRANTED else PermissionStatus.DENIED)
                }
            }

            DailyMissionCameraIntent.RetryCapture -> _state.update { it.copy(capturedUri = null) }
            DailyMissionCameraIntent.SubmitPhoto -> submitPhoto()
            DailyMissionCameraIntent.ToggleCameraLens -> _state.update { it.copy(isFrontCamera = !it.isFrontCamera) }
            DailyMissionCameraIntent.ToggleFlash -> _state.update { it.copy(isFlashEnabled = !it.isFlashEnabled) }
        }
    }

    private fun submitPhoto() {
        val currentState = state.value
        val uri = currentState.capturedUri ?: return
        val word = currentState.word
        if (word.isBlank()) return

        _state.update { it.copy(isSubmitting = true) }
        viewModelScope.launch {
            when (val result = verifyDailyMissionUseCase(uri, word)) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isSubmitting = false) }
                    if (result.data.isMatch) {
                        sendEffect(DailyMissionCameraEffect.ShowSnackbar(UiText.DynamicString("Mission completed! You earned ${result.data.xpEarned} XP and ${result.data.coinsEarned} Coins.")))
                        sendEffect(DailyMissionCameraEffect.NavigateBack)
                    } else {
                        sendEffect(DailyMissionCameraEffect.ShowSnackbar(UiText.DynamicString("That doesn't match the word. Try again!")))
                        _state.update { it.copy(capturedUri = null) }
                    }
                }

                is LinguaQuestResult.Failure -> {
                    _state.update { it.copy(isSubmitting = false, capturedUri = null) }
                    sendEffect(DailyMissionCameraEffect.ShowSnackbar(result.error.toUiText()))
                }
            }
        }
    }

    private fun sendEffect(effect: DailyMissionCameraEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
