package com.iti.linguaquest.core.appicon.rules

import com.iti.linguaquest.core.appicon.domain.AppIconEvaluation
import com.iti.linguaquest.core.appicon.domain.AppIconRule
import com.iti.linguaquest.core.appicon.domain.AppIconStateRepository
import com.iti.linguaquest.core.appicon.domain.AppIconTiming
import com.iti.linguaquest.core.appicon.domain.AppIconType
import com.iti.linguaquest.core.appicon.util.AppIconClock
import javax.inject.Inject

class AngryAppIconRule @Inject constructor(
    private val stateRepository: AppIconStateRepository,
    private val clock: AppIconClock
) : AppIconRule {
    override val priority: Int = 4200

    override suspend fun evaluate(): AppIconEvaluation? {
        val lastInteractionAt = stateRepository.snapshot().lastUserInteractionAtMillis ?: return null
        val inactive = clock.nowMillis() - lastInteractionAt
        return if (
            inactive >= AppIconTiming.ANGRY_START.inWholeMilliseconds &&
            inactive < AppIconTiming.SLEEP_START.inWholeMilliseconds
        ) {
            AppIconEvaluation(
                type = AppIconType.ANGRY,
                nextDelay = AppIconTiming.delayUntilSleep(lastInteractionAt, clock.nowMillis())
            )
        } else {
            null
        }
    }
}
