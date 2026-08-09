package com.iti.linguaquest.features.dailymission.presentation.camera.contract

import com.iti.linguaquest.core.sharedComponents.text.UiText

sealed interface DailyMissionCameraEffect {
    data object NavigateBack : DailyMissionCameraEffect
    data class ShowSnackbar(val message: UiText) : DailyMissionCameraEffect
}