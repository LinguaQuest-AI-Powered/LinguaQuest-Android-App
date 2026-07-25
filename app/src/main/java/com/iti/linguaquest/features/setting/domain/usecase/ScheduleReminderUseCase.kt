package com.iti.linguaquest.features.setting.domain.usecase

import com.iti.linguaquest.features.setting.system.AlarmScheduler
import com.iti.linguaquest.features.setting.system.ReminderSettings
import javax.inject.Inject

class ScheduleReminderUseCase @Inject constructor(
    private val alarmScheduler: AlarmScheduler
) {
    operator fun invoke(settings: ReminderSettings) {
        alarmScheduler.schedule(settings)
    }
}