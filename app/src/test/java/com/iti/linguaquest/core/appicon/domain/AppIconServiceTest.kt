package com.iti.linguaquest.core.appicon.domain

import com.iti.linguaquest.core.appicon.FakeAppIconStateRepository
import com.iti.linguaquest.core.appicon.RecordingAppIconController
import com.iti.linguaquest.core.appicon.RecordingAppIconWorkScheduler
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class AppIconServiceTest {

    @Test
    fun refresh_appliesEvaluationAndSchedulesNextRun() = runBlocking {
        val controller = RecordingAppIconController()
        val scheduler = RecordingAppIconWorkScheduler()
        val repository = FakeAppIconStateRepository()
        val engine = AppIconRuleEngine(
            listOf(
                object : AppIconRule {
                    override val priority: Int = 1

                    override suspend fun evaluate(): AppIconEvaluation? {
                        return AppIconEvaluation(
                            type = AppIconType.FIRE,
                            nextDelay = 45.seconds
                        )
                    }
                }
            )
        )

        val service = AppIconService(
            stateRepository = repository,
            ruleEngine = engine,
            manager = controller,
            workScheduler = scheduler
        )

        service.refresh()

        assertEquals(AppIconType.FIRE, controller.lastType)
        assertEquals(45.seconds, scheduler.lastScheduledNextDelay)
        assertNull(repository.consumedAchievementCount)
    }
}
