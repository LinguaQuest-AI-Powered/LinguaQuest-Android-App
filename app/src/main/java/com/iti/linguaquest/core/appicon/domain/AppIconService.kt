package com.iti.linguaquest.core.appicon.domain

import com.iti.linguaquest.core.appicon.worker.AppIconWorkScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration

@Singleton
class AppIconService @Inject constructor(
    private val stateRepository: AppIconStateRepository,
    private val ruleEngine: AppIconRuleEngine,
    private val manager: AppIconController,
    private val workScheduler: AppIconWorkScheduler
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
            val evaluation = ruleEngine.evaluate()
            Timber.d("AppIcon: ruleEngine evaluated to ${evaluation.type}")

            scheduleNextEvaluation(evaluation.nextDelay)

            val applied = manager.switchTo(evaluation.type)
            if (applied) {
                evaluation.onApplied()
            } else {
                Timber.w("App icon switch to ${evaluation.type} did not apply; will retry on next refresh")
            }
        }
    }

    private fun scheduleNextEvaluation(nextDelay: Duration?) {
        if (nextDelay == null) return

        Timber.d("AppIcon: Scheduling next evaluation in ${nextDelay.inWholeMilliseconds}ms")
        workScheduler.scheduleNextEvaluation(nextDelay)
    }
}
