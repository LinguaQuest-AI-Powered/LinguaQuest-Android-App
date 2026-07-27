package com.iti.linguaquest.features.lockscreen.presentation.contract

sealed interface LockScreenEffect {
    data object RequestNotificationPermission : LockScreenEffect
}
