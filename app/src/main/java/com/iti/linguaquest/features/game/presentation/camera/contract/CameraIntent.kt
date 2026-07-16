package com.iti.linguaquest.features.game.presentation.camera.contract

import android.net.Uri

sealed interface CameraIntent {
    data class CapturePhoto(val uri: Uri) : CameraIntent
    object ToggleFlash : CameraIntent
    object BackClicked : CameraIntent
}