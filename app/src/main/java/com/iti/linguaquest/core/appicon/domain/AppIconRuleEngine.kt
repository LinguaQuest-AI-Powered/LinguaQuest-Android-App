package com.iti.linguaquest.core.appicon.domain

import javax.inject.Inject
import kotlin.jvm.JvmSuppressWildcards

class AppIconRuleEngine @Inject constructor(
    rules: List<@JvmSuppressWildcards AppIconRule>
) {
    private val orderedRules = rules.sortedByDescending { it.priority }

     suspend fun evaluate(): AppIconDecision {
        return orderedRules.firstNotNullOfOrNull { it.evaluate() }
            ?: AppIconDecision(AppIconType.DEFAULT)
    }
}
