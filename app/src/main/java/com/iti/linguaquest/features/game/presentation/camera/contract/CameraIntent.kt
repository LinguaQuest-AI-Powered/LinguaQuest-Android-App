package com.iti.linguaquest.features.game.presentation.camera.contract

import android.net.Uri

sealed interface CameraIntent {
    data class PermissionResult(val isGranted: Boolean) : CameraIntent
    data class CapturePhoto(val uri: Uri) : CameraIntent
    object SubmitPhoto : CameraIntent
    object RetryCapture : CameraIntent
    object ToggleFlash : CameraIntent
    object BackClicked : CameraIntent
}