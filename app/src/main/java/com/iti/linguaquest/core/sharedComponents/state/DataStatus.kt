package com.iti.linguaquest.core.sharedComponents.state

import com.iti.linguaquest.core.sharedComponents.text.UiText

sealed interface DataStatus {
    data object Loading : DataStatus
    data object Loaded : DataStatus
    data object Refreshing : DataStatus
    data class Error(val message: UiText) : DataStatus
}
