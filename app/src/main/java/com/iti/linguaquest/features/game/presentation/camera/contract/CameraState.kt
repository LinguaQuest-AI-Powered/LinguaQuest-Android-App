package com.iti.linguaquest.features.game.presentation.camera.contract

import android.net.Uri

data class CameraState(
    val hasPermission: Boolean = false,
    val isFlashEnabled: Boolean = false,
    val isFrontCamera: Boolean = false,
    val capturedUri: Uri? = null
)