package com.iti.linguaquest.features.setting.domain.usecase

import com.iti.linguaquest.features.setting.system.AlarmScheduler
import javax.inject.Inject

class CancelReminderUseCase @Inject constructor(
    private val alarmScheduler: AlarmScheduler
) {
    operator fun invoke() {
        alarmScheduler.cancel()
    }
}