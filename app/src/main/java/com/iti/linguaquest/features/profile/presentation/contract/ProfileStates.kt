package com.iti.linguaquest.features.profile.presentation.contract

import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.profile.presentation.model.ProfileState

import com.iti.linguaquest.core.sharedComponents.state.DataStatus

data class ProfileUiState(
    val dataStatus: DataStatus = DataStatus.Loading,
    val profile: ProfileState = ProfileState(),
    val isAvatarUploading: Boolean = false
) {
    val hasData: Boolean get() = profile.userName.isNotBlank()
}