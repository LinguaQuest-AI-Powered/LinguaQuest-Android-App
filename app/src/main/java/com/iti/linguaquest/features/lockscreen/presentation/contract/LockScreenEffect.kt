package com.iti.linguaquest.features.lockscreen.presentation.contract

sealed interface LockScreenEffect {
    data object RequestNotificationPermission : LockScreenEffect
    data class ShowMessage(val message: String) : LockScreenEffect
}
