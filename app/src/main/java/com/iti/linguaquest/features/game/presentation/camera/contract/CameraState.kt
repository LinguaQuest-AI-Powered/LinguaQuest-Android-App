package com.iti.linguaquest.features.game.presentation.camera.contract

import android.net.Uri

data class CameraState(
    val permissionStatus: PermissionStatus = PermissionStatus.IDLE,
    val isFlashEnabled: Boolean = false,
    val isFrontCamera: Boolean = false,
    val capturedUri: Uri? = null,
    val isSubmitting: Boolean = false
)

enum class PermissionStatus {
    IDLE,
    GRANTED,
    DENIED,
    PERMANENTLY_DENIED
}