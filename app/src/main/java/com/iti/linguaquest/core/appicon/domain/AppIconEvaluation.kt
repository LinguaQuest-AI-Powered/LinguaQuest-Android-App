package com.iti.linguaquest.core.appicon.domain

import kotlin.time.Duration

data class AppIconEvaluation(
    val type: AppIconType,
    val nextDelay: Duration? = null,
    val onApplied: suspend () -> Unit = {}
)
