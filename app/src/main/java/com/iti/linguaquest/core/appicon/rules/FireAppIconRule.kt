package com.iti.linguaquest.core.appicon.rules

import com.iti.linguaquest.core.appicon.domain.AppIconDecision
import com.iti.linguaquest.core.appicon.domain.AppIconRule
import com.iti.linguaquest.core.appicon.domain.AppIconStateRepository
import com.iti.linguaquest.core.appicon.domain.AppIconType
import javax.inject.Inject

class FireAppIconRule @Inject constructor(
    private val stateRepository: AppIconStateRepository
) : AppIconRule {
    override val priority: Int = 4000

    override suspend fun evaluate(): AppIconDecision? {
        val streakDays = stateRepository.snapshot().observedStreakDays
        return if (streakDays >= 2) AppIconDecision(AppIconType.FIRE) else null
    }
}
