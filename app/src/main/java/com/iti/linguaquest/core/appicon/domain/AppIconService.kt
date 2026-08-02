package com.iti.linguaquest.core.appicon.domain

import com.iti.linguaquest.core.appicon.util.AppIconClock
import com.iti.linguaquest.core.appicon.worker.AppIconWorkScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppIconService @Inject constructor(
    private val stateRepository: AppIconStateRepository,
    private val ruleEngine: AppIconRuleEngine,
    private val manager: AppIconController,
    private val workScheduler: AppIconWorkScheduler,
    private val clock: AppIconClock
) {
    suspend fun onAppBackgrounded() {
        withContext(Dispatchers.IO) {
            Timber.d("AppIcon: onAppBackgrounded called")
            stateRepository.markUserInteraction()
        }
    }

    suspend fun saveHomeSnapshot(streakDays: Int) {
        withContext(Dispatchers.IO) {
            stateRepository.observeHomeSnapshot(streakDays)
        }
    }

    suspend fun saveProfileSnapshot(streakDays: Int, achievementCount: Int) {
        withContext(Dispatchers.IO) {
            stateRepository.observeProfileSnapshot(streakDays, achievementCount)
        }
    }

    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            Timber.d("AppIcon: refresh called by worker")
            applyIcon()
        }
    }

    private suspend fun applyIcon() {
        val decision = ruleEngine.evaluate()
        Timber.d("AppIcon: ruleEngine evaluated to ${decision.type}")
        
        scheduleFollowUpIfNeeded(decision.type)

        val applied = manager.switchTo(decision.type)

        if (applied) {
            decision.onApplied()
        } else {
            Timber.w("App icon switch to ${decision.type} did not apply; will retry on next refresh")
        }
    }

    private suspend fun scheduleFollowUpIfNeeded(currentType: AppIconType) {
        val lastInteractionAt = stateRepository.snapshot().lastUserInteractionAtMillis ?: return

        when (currentType) {
            AppIconType.FIRE, AppIconType.DEFAULT, AppIconType.REWARD -> {
                val angryStartsAtMillis = lastInteractionAt +
                    TimeUnit.MINUTES.toMillis(AppIconTiming.ANGRY_START_MINUTES)
                val delayMillis = (angryStartsAtMillis - clock.nowMillis()).coerceAtLeast(0L)
                Timber.d("AppIcon: Scheduling ANGRY follow-up in ${delayMillis}ms")
                workScheduler.scheduleFollowUpCheck(delayMillis)
            }
            AppIconType.ANGRY -> {
                val sleepStartsAtMillis = lastInteractionAt +
                    TimeUnit.MINUTES.toMillis(AppIconTiming.SLEEP_START_MINUTES)
                val delayMillis = (sleepStartsAtMillis - clock.nowMillis()).coerceAtLeast(0L)
                Timber.d("AppIcon: Scheduling SLEEP follow-up in ${delayMillis}ms")
                workScheduler.scheduleFollowUpCheck(delayMillis)
            }
            AppIconType.SLEEP -> Unit
        }
    }
}
