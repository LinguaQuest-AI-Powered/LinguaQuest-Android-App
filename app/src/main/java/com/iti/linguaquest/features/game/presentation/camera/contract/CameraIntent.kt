package com.iti.linguaquest.features.game.presentation.camera.contract

import android.net.Uri

sealed interface CameraIntent {
    data class PermissionResult(val status: PermissionStatus) : CameraIntent
    object GrantPermissionClicked : CameraIntent
    data class CapturePhoto(val uri: Uri) : CameraIntent
    data class SubmitPhoto(val worldId: Int = 1, val levelId: Int = 1) : CameraIntent
    object RetryCapture : CameraIntent
    object ToggleFlash : CameraIntent
    object BackClicked : CameraIntent
    object ToggleCameraLens : CameraIntent
}