package com.iti.linguaquest.core.appicon.util

import javax.inject.Inject

interface AppIconClock {
    fun nowMillis(): Long
}

class SystemAppIconClock @Inject constructor() : AppIconClock {
    override fun nowMillis(): Long = System.currentTimeMillis()
}
