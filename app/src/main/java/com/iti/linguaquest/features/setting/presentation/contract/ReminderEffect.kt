package com.iti.linguaquest.features.setting.presentation.contract

sealed interface ReminderEffect {
    data object ReminderEnabled : ReminderEffect
    data object ReminderDisabled : ReminderEffect
    data object ReminderUpdated : ReminderEffect
}
