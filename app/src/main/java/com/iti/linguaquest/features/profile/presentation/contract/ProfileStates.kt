package com.iti.linguaquest.features.profile.presentation.contract

import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.profile.presentation.model.ProfileState

sealed interface ProfileDataStatus {
    data object Loading : ProfileDataStatus
    data object Loaded : ProfileDataStatus
    data object Refreshing : ProfileDataStatus
    data class Error(val message: UiText) : ProfileDataStatus
}

data class ProfileUiState(
    val dataStatus: ProfileDataStatus = ProfileDataStatus.Loading,
    val profile: ProfileState = ProfileState(),
    val isAvatarUploading: Boolean = false
) {
    val hasData: Boolean get() = profile.userName.isNotBlank()
}