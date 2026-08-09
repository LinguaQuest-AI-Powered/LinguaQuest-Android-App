package com.iti.linguaquest.features.lockscreen.presentation.contract

sealed interface LockScreenEffect {
    data object RequestNotificationPermission : LockScreenEffect
    data object PlayCoinDeductedSound : LockScreenEffect
}
