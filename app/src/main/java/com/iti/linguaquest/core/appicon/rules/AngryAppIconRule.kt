package com.iti.linguaquest.core.appicon.rules

import com.iti.linguaquest.core.appicon.domain.AppIconClock
import com.iti.linguaquest.core.appicon.domain.AppIconRule
import com.iti.linguaquest.core.appicon.domain.AppIconStateRepository
import com.iti.linguaquest.core.appicon.domain.AppIconType
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AngryAppIconRule @Inject constructor(
    private val stateRepository: AppIconStateRepository,
    private val clock: AppIconClock
) : AppIconRule {
    override val priority: Int = 2000

    override suspend fun evaluate(): AppIconType? {
        val lastInteractionAt = stateRepository.snapshot().lastUserInteractionAtMillis ?: return null
        val inactiveDays = TimeUnit.MILLISECONDS.toDays(clock.nowMillis() - lastInteractionAt)
        return if (inactiveDays == 1L) AppIconType.ANGRY else null
    }
}