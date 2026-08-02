package com.iti.linguaquest.core.appicon.domain

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object AppIconTiming {
    val ANGRY_START: Duration = 1.minutes
    val SLEEP_START: Duration = 3.minutes
    val ANGRY_CHECK_DELAY: Duration = 100.seconds
    val BACKGROUND_EXIT_DELAY: Duration = 2.seconds
    val PERIODIC_REFRESH_INTERVAL: Duration = 15.minutes

    fun delayUntilAngry(lastInteractionAtMillis: Long, nowMillis: Long): Duration {
        return remainingDelay(
            lastInteractionAtMillis = lastInteractionAtMillis,
            nowMillis = nowMillis,
            threshold = ANGRY_START
        )
    }

    fun delayUntilSleep(lastInteractionAtMillis: Long, nowMillis: Long): Duration {
        return remainingDelay(
            lastInteractionAtMillis = lastInteractionAtMillis,
            nowMillis = nowMillis,
            threshold = SLEEP_START
        )
    }

    private fun remainingDelay(
        lastInteractionAtMillis: Long,
        nowMillis: Long,
        threshold: Duration
    ): Duration {
        val elapsed = (nowMillis - lastInteractionAtMillis).milliseconds
        return (threshold - elapsed).coerceAtLeast(0.seconds)
    }
}
