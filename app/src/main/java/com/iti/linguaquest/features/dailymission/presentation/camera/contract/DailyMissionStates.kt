package com.iti.linguaquest.features.dailymission.presentation.camera.contract

import android.net.Uri
import com.iti.linguaquest.features.game.presentation.camera.contract.PermissionStatus

data class DailyMissionCameraState(
    val word: String = "",
    val permissionStatus: PermissionStatus = PermissionStatus.IDLE,
    val capturedUri: Uri? = null,
    val isFlashEnabled: Boolean = false,
    val isFrontCamera: Boolean = false,
    val isSubmitting: Boolean = false
)