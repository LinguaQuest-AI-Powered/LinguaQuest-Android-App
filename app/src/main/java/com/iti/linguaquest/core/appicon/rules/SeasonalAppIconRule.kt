package com.iti.linguaquest.core.appicon.rules

import com.iti.linguaquest.core.appicon.domain.AppIconEvaluation
import com.iti.linguaquest.core.appicon.domain.AppIconRule
import com.iti.linguaquest.core.appicon.domain.SeasonalIconWindow
import com.iti.linguaquest.core.appicon.util.AppIconClock
import java.util.Calendar
import javax.inject.Inject

class SeasonalAppIconRule @Inject constructor(
    private val clock: AppIconClock,
    private val windows: List<SeasonalIconWindow>
) : AppIconRule {
    override val priority: Int = 1000

    override suspend fun evaluate(): AppIconEvaluation? {
        if (windows.isEmpty()) return null

        val calendar = Calendar.getInstance().apply { timeInMillis = clock.nowMillis() }
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val matched = windows.firstOrNull { window ->
            isInRange(month, day, window.startMonth, window.startDay, window.endMonth, window.endDay)
        } ?: return null

        return AppIconEvaluation(matched.iconType)
    }

    private fun isInRange(
        month: Int,
        day: Int,
        startMonth: Int,
        startDay: Int,
        endMonth: Int,
        endDay: Int
    ): Boolean {
        val current = month * 100 + day
        val start = startMonth * 100 + startDay
        val end = endMonth * 100 + endDay

        return if (start <= end) {
            current in start..end
        } else {
            current !in (end + 1)..<start
        }
    }
}
