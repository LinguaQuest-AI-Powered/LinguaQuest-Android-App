package com.iti.linguaquest.features.lockscreen.presentation.contract

import com.iti.linguaquest.core.sharedComponents.text.UiText

sealed interface LockScreenEffect {
    data object RequestNotificationPermission : LockScreenEffect
    data class ShowMessage(val message: UiText) : LockScreenEffect
}
