package com.iti.linguaquest.core.appicon.util

import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppForegroundTracker @Inject constructor() {
    private val startedActivityCount = AtomicInteger(0)

    val isInForeground: Boolean
        get() = startedActivityCount.get() > 0

    fun onActivityStarted() {
        startedActivityCount.incrementAndGet()
    }

    fun onActivityStopped() {
        startedActivityCount.updateAndGet { count -> maxOf(0, count - 1) }
    }
}