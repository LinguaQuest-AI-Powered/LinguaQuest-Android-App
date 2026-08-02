package com.iti.linguaquest.core.appicon.worker

import kotlin.time.Duration

interface AppIconWorkScheduler {

    fun scheduleDailyRefresh()

    fun scheduleAngryWindowCheck()

    fun scheduleNextEvaluation(nextDelay: Duration)

    fun scheduleBackgroundExitCheck()

    fun cancelBackgroundExitCheck()
}
