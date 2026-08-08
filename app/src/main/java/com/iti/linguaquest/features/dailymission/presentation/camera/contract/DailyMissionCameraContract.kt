package com.iti.linguaquest.features.dailymission.presentation.camera.contract

import android.net.Uri
import com.iti.linguaquest.features.game.presentation.camera.contract.PermissionStatus
import com.iti.linguaquest.core.sharedComponents.text.UiText

data class DailyMissionCameraState(
    val word: String = "",
    val permissionStatus: PermissionStatus = PermissionStatus.IDLE,
    val capturedUri: Uri? = null,
    val isFlashEnabled: Boolean = false,
    val isFrontCamera: Boolean = false,
    val isSubmitting: Boolean = false
)

sealed interface DailyMissionCameraIntent {
    data class InitWord(val word: String) : DailyMissionCameraIntent
    data class PermissionResult(val isGranted: Boolean) : DailyMissionCameraIntent
    data object GrantPermissionClicked : DailyMissionCameraIntent
    data object ToggleFlash : DailyMissionCameraIntent
    data object ToggleCameraLens : DailyMissionCameraIntent
    data class CapturePhoto(val uri: Uri) : DailyMissionCameraIntent
    data object RetryCapture : DailyMissionCameraIntent
    data object SubmitPhoto : DailyMissionCameraIntent
    data object BackClicked : DailyMissionCameraIntent
}

sealed interface DailyMissionCameraEffect {
    data object NavigateBack : DailyMissionCameraEffect
    data class ShowSnackbar(val message: UiText) : DailyMissionCameraEffect
}
