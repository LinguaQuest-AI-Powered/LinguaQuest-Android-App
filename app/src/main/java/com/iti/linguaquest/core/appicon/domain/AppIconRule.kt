package com.iti.linguaquest.core.appicon.domain

interface AppIconRule {
    val priority: Int

    suspend fun evaluate(): AppIconDecision?
}
