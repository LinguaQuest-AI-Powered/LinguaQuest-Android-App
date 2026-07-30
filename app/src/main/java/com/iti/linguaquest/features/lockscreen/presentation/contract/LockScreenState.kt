package com.iti.linguaquest.features.lockscreen.presentation.contract

import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenFeatureState

data class LockScreenState(
    val featureState: LockScreenFeatureState = LockScreenFeatureState.DISABLED,
    val isNotificationPermissionGranted: Boolean = false,
    val isConfirmDialogVisible: Boolean = false,
    val isLoading: Boolean = false,
    val pendingCount: Int = 0,
    val batchSize: Int = 30,
    val pendingGeneration: Boolean = false,
    val lastGenerationTime: Long? = null,
    val lastNativeLanguage: String? = null,
    val lastTargetLanguage: String? = null,
    val lastProficiencyLevel: String? = null,
    val currentNativeLanguage: String? = null,
    val currentTargetLanguage: String? = null,
    val currentProficiencyLevel: String? = null,
    val pendingOperationId: String? = null,
    val errorMessage: UiText? = null,
)
