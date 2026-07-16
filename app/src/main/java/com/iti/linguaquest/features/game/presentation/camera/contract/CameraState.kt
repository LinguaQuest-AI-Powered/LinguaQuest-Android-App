package com.iti.linguaquest.features.game.presentation.camera.contract

data class CameraState(
    val hasPermission: Boolean = false,
    val isFlashEnabled: Boolean = false
)