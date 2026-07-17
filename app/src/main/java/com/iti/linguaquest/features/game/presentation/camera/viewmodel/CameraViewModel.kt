package com.iti.linguaquest.features.game.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.game.presentation.camera.contract.CameraEffect
import com.iti.linguaquest.features.game.presentation.camera.contract.CameraIntent
import com.iti.linguaquest.features.game.presentation.camera.contract.CameraState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(CameraState())
    val state: StateFlow<CameraState> = _state.asStateFlow()

    private val _effect = Channel<CameraEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: CameraIntent) {
        when (intent) {
            is CameraIntent.PermissionResult -> {
                _state.update { it.copy(hasPermission = intent.isGranted) }
            }
            is CameraIntent.CapturePhoto -> {
                sendEffect(CameraEffect.NavigateToProcessing(intent.uri))
            }
            CameraIntent.ToggleFlash -> {
                _state.update { it.copy(isFlashEnabled = !it.isFlashEnabled) }
            }
            CameraIntent.BackClicked -> {
                sendEffect(CameraEffect.NavigateBack)
            }
        }
    }

    private fun sendEffect(effect: CameraEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}