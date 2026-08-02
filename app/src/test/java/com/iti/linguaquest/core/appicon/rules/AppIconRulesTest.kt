package com.iti.linguaquest.core.appicon.rules

import com.iti.linguaquest.core.appicon.FakeAppIconStateRepository
import com.iti.linguaquest.core.appicon.FixedAppIconClock
import com.iti.linguaquest.core.appicon.domain.AppIconSnapshot
import com.iti.linguaquest.core.appicon.domain.AppIconTiming
import com.iti.linguaquest.core.appicon.domain.AppIconType
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AppIconRulesTest {

    private val nowMillis = 1_000_000L
    private val clock = FixedAppIconClock(nowMillis)

    @Test
    fun defaultRule_returnsDefaultAndSchedulesAngryCheck() = runBlocking {
        val repository = FakeAppIconStateRepository(
            AppIconSnapshot(lastUserInteractionAtMillis = nowMillis - 30_000L)
        )
        val rule = DefaultAppIconRule(repository, clock)

        val evaluation = rule.evaluate()

        assertEquals(AppIconType.DEFAULT, evaluation.type)
        assertEquals(30_000L, evaluation.nextDelay?.inWholeMilliseconds)
    }

    @Test
    fun fireRule_returnsFireAndSchedulesAngryCheck() = runBlocking {
        val repository = FakeAppIconStateRepository(
            AppIconSnapshot(
                lastUserInteractionAtMillis = nowMillis - 10_000L,
                observedStreakDays = 3
            )
        )
        val rule = FireAppIconRule(repository, clock)

        val evaluation = rule.evaluate()

        assertEquals(AppIconType.FIRE, evaluation?.type)
        assertEquals(50_000L, evaluation?.nextDelay?.inWholeMilliseconds)
    }

    @Test
    fun angryRule_returnsAngryAndSchedulesSleepCheck() = runBlocking {
        val repository = FakeAppIconStateRepository(
            AppIconSnapshot(lastUserInteractionAtMillis = nowMillis - 90_000L)
        )
        val rule = AngryAppIconRule(repository, clock)

        val evaluation = rule.evaluate()

        assertEquals(AppIconType.ANGRY, evaluation?.type)
        assertEquals(90_000L, evaluation?.nextDelay?.inWholeMilliseconds)
    }

    @Test
    fun sleepRule_returnsSleepWithoutNextDelay() = runBlocking {
        val repository = FakeAppIconStateRepository(
            AppIconSnapshot(lastUserInteractionAtMillis = nowMillis - AppIconTiming.SLEEP_START.inWholeMilliseconds)
        )
        val rule = SleepAppIconRule(repository, clock)

        val evaluation = rule.evaluate()

        assertEquals(AppIconType.SLEEP, evaluation?.type)
        assertNull(evaluation?.nextDelay)
    }

    @Test
    fun rewardRule_returnsRewardAndConsumesAchievementsOnApply() = runBlocking {
        val repository = FakeAppIconStateRepository(
            AppIconSnapshot(
                lastUserInteractionAtMillis = nowMillis - 20_000L,
                observedAchievementCount = 5,
                consumedAchievementCount = 2
            )
        )
        val rule = RewardAppIconRule(repository, clock)

        val evaluation = rule.evaluate()

        assertEquals(AppIconType.REWARD, evaluation?.type)
        assertEquals(40_000L, evaluation?.nextDelay?.inWholeMilliseconds)

        evaluation?.onApplied?.invoke()
        assertEquals(5, repository.consumedAchievementCount)
    }
}
