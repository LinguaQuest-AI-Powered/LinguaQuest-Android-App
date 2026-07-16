package com.iti.linguaquest.features.game.presentation.camera.contract

import android.net.Uri

sealed interface CameraEffect {
    data class NavigateToProcessing(val imageUri: Uri) : CameraEffect
    object NavigateBack : CameraEffect
}