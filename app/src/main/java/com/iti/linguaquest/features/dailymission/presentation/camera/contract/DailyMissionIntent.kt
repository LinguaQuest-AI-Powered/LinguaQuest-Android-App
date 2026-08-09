package com.iti.linguaquest.features.dailymission.presentation.camera.contract

import android.net.Uri

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