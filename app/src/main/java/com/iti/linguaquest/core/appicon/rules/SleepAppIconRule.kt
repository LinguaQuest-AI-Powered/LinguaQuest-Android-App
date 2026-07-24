package com.iti.linguaquest.core.appicon.rules

import com.iti.linguaquest.core.appicon.domain.AppIconRule
import com.iti.linguaquest.core.appicon.domain.AppIconStateRepository
import com.iti.linguaquest.core.appicon.domain.AppIconType
import com.iti.linguaquest.core.appicon.util.AppIconClock
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SleepAppIconRule @Inject constructor(
    private val stateRepository: AppIconStateRepository,
    private val clock: AppIconClock
) : AppIconRule {
    override val priority: Int = 3000

    override suspend fun evaluate(): AppIconType? {
        val lastInteractionAt = stateRepository.snapshot().lastUserInteractionAtMillis ?: return null
        val inactiveDays = TimeUnit.MILLISECONDS.toDays(clock.nowMillis() - lastInteractionAt)
        return if (inactiveDays >= 2) AppIconType.SLEEP else null
    }
}
