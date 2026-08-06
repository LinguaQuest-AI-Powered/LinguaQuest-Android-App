package com.iti.linguaquest.features.setting.domain.usecase

import com.iti.linguaquest.features.setting.system.AlarmScheduler
import com.iti.linguaquest.features.setting.system.ReminderSettings
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.time.DayOfWeek

class ScheduleReminderUseCaseTest {

    private lateinit var alarmScheduler: AlarmScheduler
    private lateinit var useCase: ScheduleReminderUseCase

    @Before
    fun setUp() {
        // Given
        alarmScheduler = mockk(relaxed = true)
        useCase = ScheduleReminderUseCase(alarmScheduler)
    }

    @Test
    fun invoke_callsScheduleOnAlarmScheduler_whenCalled() {
        // Given
        val settings = ReminderSettings(
            enabled = true,
            hour = 8,
            minute = 30,
            selectedDays = setOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)
        )

        // When
        useCase(settings)

        // Then
        verify { alarmScheduler.schedule(settings) }
    }
}
