package com.iti.linguaquest.core.appicon.rules

import com.iti.linguaquest.core.appicon.domain.AppIconEvaluation
import com.iti.linguaquest.core.appicon.domain.AppIconRule
import com.iti.linguaquest.core.appicon.domain.AppIconStateRepository
import com.iti.linguaquest.core.appicon.domain.AppIconTiming
import com.iti.linguaquest.core.appicon.domain.AppIconType
import com.iti.linguaquest.core.appicon.util.AppIconClock
import javax.inject.Inject

class SleepAppIconRule @Inject constructor(
    private val stateRepository: AppIconStateRepository,
    private val clock: AppIconClock
) : AppIconRule {
    override val priority: Int = 4500

    override suspend fun evaluate(): AppIconEvaluation? {
        val lastInteractionAt = stateRepository.snapshot().lastUserInteractionAtMillis ?: return null
        val inactive = clock.nowMillis() - lastInteractionAt
        return if (inactive >= AppIconTiming.SLEEP_START.inWholeMilliseconds) {
            AppIconEvaluation(AppIconType.SLEEP)
        } else {
            null
        }
    }
}
