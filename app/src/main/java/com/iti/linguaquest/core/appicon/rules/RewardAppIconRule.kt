package com.iti.linguaquest.core.appicon.rules

import com.iti.linguaquest.core.appicon.domain.AppIconEvaluation
import com.iti.linguaquest.core.appicon.domain.AppIconRule
import com.iti.linguaquest.core.appicon.domain.AppIconStateRepository
import com.iti.linguaquest.core.appicon.domain.AppIconTiming
import com.iti.linguaquest.core.appicon.domain.AppIconType
import com.iti.linguaquest.core.appicon.util.AppIconClock
import javax.inject.Inject

class RewardAppIconRule @Inject constructor(
    private val stateRepository: AppIconStateRepository,
    private val clock: AppIconClock
) : AppIconRule {
    override val priority: Int = 5000

    override suspend fun evaluate(): AppIconEvaluation? {
        val snapshot = stateRepository.snapshot()
        if (snapshot.observedAchievementCount <= snapshot.consumedAchievementCount) {
            return null
        }

        val nextDelay = snapshot.lastUserInteractionAtMillis?.let {
            AppIconTiming.delayUntilAngry(it, clock.nowMillis())
        }

        return AppIconEvaluation(
            type = AppIconType.REWARD,
            nextDelay = nextDelay,
            onApplied = {
                stateRepository.consumeAchievements(snapshot.observedAchievementCount)
            }
        )
    }
}
