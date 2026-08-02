package com.iti.linguaquest.core.appicon

import com.iti.linguaquest.core.appicon.domain.AppIconController
import com.iti.linguaquest.core.appicon.domain.AppIconSnapshot
import com.iti.linguaquest.core.appicon.domain.AppIconStateRepository
import com.iti.linguaquest.core.appicon.domain.AppIconType
import com.iti.linguaquest.core.appicon.util.AppIconClock
import com.iti.linguaquest.core.appicon.worker.AppIconWorkScheduler
import kotlin.time.Duration

internal class FakeAppIconStateRepository(
    var snapshotState: AppIconSnapshot = AppIconSnapshot()
) : AppIconStateRepository {
    var markUserInteractionCalls = 0
    var consumedAchievementCount: Int? = null

    override suspend fun snapshot(): AppIconSnapshot = snapshotState

    override suspend fun markUserInteraction() {
        markUserInteractionCalls += 1
    }

    override suspend fun observeHomeSnapshot(streakDays: Int) {
        snapshotState = snapshotState.copy(observedStreakDays = streakDays)
    }

    override suspend fun observeProfileSnapshot(streakDays: Int, achievementCount: Int) {
        snapshotState = snapshotState.copy(
            observedStreakDays = streakDays,
            observedAchievementCount = achievementCount
        )
    }

    override suspend fun consumeAchievements(upToCount: Int) {
        consumedAchievementCount = upToCount
        snapshotState = snapshotState.copy(consumedAchievementCount = upToCount)
    }
}

internal class FixedAppIconClock(
    private val nowMillisValue: Long
) : AppIconClock {
    override fun nowMillis(): Long = nowMillisValue
}

internal class RecordingAppIconController : AppIconController {
    var lastType: AppIconType? = null
    var switchResult: Boolean = true

    override fun switchTo(type: AppIconType): Boolean {
        lastType = type
        return switchResult
    }
}

internal class RecordingAppIconWorkScheduler : AppIconWorkScheduler {
    var lastScheduledNextDelay: Duration? = null
    var dailyRefreshScheduled = false
    var angryWindowCheckScheduled = false
    var backgroundExitCheckScheduled = false
    var backgroundExitCheckCancelled = false

    override fun scheduleDailyRefresh() {
        dailyRefreshScheduled = true
    }

    override fun scheduleAngryWindowCheck() {
        angryWindowCheckScheduled = true
    }

    override fun scheduleNextEvaluation(nextDelay: Duration) {
        lastScheduledNextDelay = nextDelay
    }

    override fun scheduleBackgroundExitCheck() {
        backgroundExitCheckScheduled = true
    }

    override fun cancelBackgroundExitCheck() {
        backgroundExitCheckCancelled = true
    }
}
