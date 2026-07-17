package com.iti.linguaquest.features.game.presentation.camera.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.game.presentation.camera.contract.CameraEffect
import com.iti.linguaquest.features.game.presentation.camera.contract.CameraIntent
import com.iti.linguaquest.features.game.presentation.camera.contract.CameraState
import com.iti.linguaquest.features.game.presentation.camera.contract.PermissionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
                _state.update { it.copy(permissionStatus = intent.status) }
            }
            CameraIntent.GrantPermissionClicked -> {
                if (_state.value.permissionStatus == PermissionStatus.PERMANENTLY_DENIED) {
                    sendEffect(CameraEffect.OpenAppSettings)
                } else {
                    sendEffect(CameraEffect.RequestCameraPermission)
                }
            }
            is CameraIntent.CapturePhoto -> {
                _state.update { it.copy(capturedUri = intent.uri) }
            }
            CameraIntent.RetryCapture -> {
                retryCapture()
            }
            CameraIntent.SubmitPhoto -> {
                _state.value.capturedUri?.let { uri ->
                    sendEffect(CameraEffect.NavigateToProcessing(uri))
                }
            }
            CameraIntent.ToggleCameraLens -> {
                _state.update { it.copy(isFrontCamera = !it.isFrontCamera) }
            }
            CameraIntent.ToggleFlash -> {
                _state.update { it.copy(isFlashEnabled = !it.isFlashEnabled) }
            }
            CameraIntent.BackClicked -> {
                sendEffect(CameraEffect.NavigateBack)
            }
        }
    }

    private fun retryCapture(){
        val currentUri = _state.value.capturedUri

        _state.update { it.copy(capturedUri = null) }

        currentUri?.path?.let { path ->
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val file = java.io.File(path)
                    if (file.exists()) {
                        file.delete()
                    }
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun sendEffect(effect: CameraEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}