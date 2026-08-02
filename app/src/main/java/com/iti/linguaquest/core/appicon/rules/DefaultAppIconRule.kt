package com.iti.linguaquest.core.appicon.rules

import com.iti.linguaquest.core.appicon.domain.AppIconEvaluation
import com.iti.linguaquest.core.appicon.domain.AppIconRule
import com.iti.linguaquest.core.appicon.domain.AppIconStateRepository
import com.iti.linguaquest.core.appicon.domain.AppIconTiming
import com.iti.linguaquest.core.appicon.domain.AppIconType
import com.iti.linguaquest.core.appicon.util.AppIconClock
import javax.inject.Inject

class DefaultAppIconRule @Inject constructor(
    private val stateRepository: AppIconStateRepository,
    private val clock: AppIconClock
) : AppIconRule {
    override val priority: Int = Int.MIN_VALUE

    override suspend fun evaluate(): AppIconEvaluation {
        val lastInteractionAt = stateRepository.snapshot().lastUserInteractionAtMillis
        val nextDelay = lastInteractionAt?.let {
            AppIconTiming.delayUntilAngry(it, clock.nowMillis())
        }
        return AppIconEvaluation(AppIconType.DEFAULT, nextDelay)
    }
}
