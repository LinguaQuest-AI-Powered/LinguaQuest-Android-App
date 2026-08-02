package com.iti.linguaquest.core.appicon.domain

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class AppIconRuleEngineTest {

    @Test
    fun evaluate_returnsHighestPriorityMatchingRule() = runBlocking {
        val engine = AppIconRuleEngine(
            listOf(
                FakeRule(priority = 10, evaluation = AppIconEvaluation(AppIconType.FIRE)),
                FakeRule(priority = 100, evaluation = AppIconEvaluation(AppIconType.REWARD))
            )
        )

        val evaluation = engine.evaluate()

        assertEquals(AppIconType.REWARD, evaluation.type)
    }

    @Test
    fun evaluate_fallsBackToDefaultWhenNoRuleMatches() = runBlocking {
        val engine = AppIconRuleEngine(
            listOf(
                FakeRule(priority = 10, evaluation = null)
            )
        )

        val evaluation = engine.evaluate()

        assertEquals(AppIconType.DEFAULT, evaluation.type)
    }

    private class FakeRule(
        override val priority: Int,
        private val evaluation: AppIconEvaluation?
    ) : AppIconRule {
        override suspend fun evaluate(): AppIconEvaluation? = evaluation
    }
}
