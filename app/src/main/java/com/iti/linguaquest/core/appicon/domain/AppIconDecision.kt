package com.iti.linguaquest.core.appicon.domain


data class AppIconDecision(
    val type: AppIconType,
    val onApplied: suspend () -> Unit = {}
)
