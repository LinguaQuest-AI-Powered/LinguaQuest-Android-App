package com.iti.linguaquest.features.game.presentation.camera.contract

import android.net.Uri

sealed interface CameraIntent {
    data class CapturePhoto(val uri: Uri) : CameraIntent
    data class PermissionResult(val isGranted: Boolean) : CameraIntent
    object ToggleFlash : CameraIntent
    object BackClicked : CameraIntent
}