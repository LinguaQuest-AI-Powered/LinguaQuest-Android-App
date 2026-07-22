package com.iti.linguaquest.core.appicon.rules

import com.iti.linguaquest.core.appicon.domain.AppIconRule
import com.iti.linguaquest.core.appicon.domain.AppIconStateRepository
import com.iti.linguaquest.core.appicon.domain.AppIconType
import javax.inject.Inject

class RewardAppIconRule @Inject constructor(
    private val stateRepository: AppIconStateRepository
) : AppIconRule {
    override val priority: Int = 5000

    override suspend fun evaluate(): AppIconType? {
        val snapshot = stateRepository.snapshot()
        if (snapshot.observedAchievementCount <= snapshot.consumedAchievementCount) {
            return null
        }

        stateRepository.consumeAchievements(snapshot.observedAchievementCount)
        return AppIconType.REWARD
    }
}
