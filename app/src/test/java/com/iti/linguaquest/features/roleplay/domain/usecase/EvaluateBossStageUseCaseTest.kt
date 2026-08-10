package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.features.roleplay.domain.model.BossEvaluationResult
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.ChatMessage
import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EvaluateBossStageUseCaseTest {

    private lateinit var repository: RoleplayRepository
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var useCase: EvaluateBossStageUseCase

    private val sampleScenario = BossScenario(
        id = ScenarioId.SCENARIO_CAFE_01,
        worldId = "world_1",
        bossName = "Pierre",
        roleDescription = "Barista",
        objective = "Order coffee in French",
        voiceName = "Puck"
    )

    @Before
    fun setUp() {
        repository = mockk()
        userPreferencesRepository = mockk()

        every { userPreferencesRepository.targetLanguageName } returns flowOf("French")
        useCase = EvaluateBossStageUseCase(repository, userPreferencesRepository)
    }

    @Test
    fun invoke_returnsNoSpeechError_whenUserHasNotSpoken() = runTest {
        // Given
        val messages = listOf(
            ChatMessage("Bonjour! Que puis-je vous servir?", isUser = false)
        )

        // When
        val result = useCase(messages, sampleScenario)

        // Then
        assertTrue(result.isSuccess)
        val evaluation = result.getOrThrow()
        assertFalse(evaluation.task_completed)
        assertEquals(0, evaluation.fluency_score)
        assertEquals("ERROR_NO_SPEECH", evaluation.feedback_message)
    }

    @Test
    fun invoke_awards3StarsAndMaxRewards_whenScoreIs85OrHigherAndTaskCompleted() = runTest {
        // Given
        val messages = listOf(
            ChatMessage("Bonjour, je voudrais un cafe s'il vous plait", isUser = true),
            ChatMessage("Tres bien, avec du sucre?", isUser = false),
            ChatMessage("Oui s'il vous plait", isUser = true)
        )
        val rawEvaluation = BossEvaluationResult(
            task_completed = true,
            fluency_score = 90,
            target_language_percentage = 95,
            improvements = listOf("Bien dit")
        )
        coEvery { repository.evaluateBossStage(any(), sampleScenario) } returns Result.success(rawEvaluation)

        // When
        val result = useCase(messages, sampleScenario)

        // Then
        assertTrue(result.isSuccess)
        val evaluation = result.getOrThrow()
        assertTrue(evaluation.task_completed)
        assertEquals(90, evaluation.fluency_score)
        assertEquals(3, evaluation.stars)
        assertEquals(15, evaluation.xp_earned)
        assertEquals(3, evaluation.coins_earned)
    }

    @Test
    fun invoke_awards2StarsAndMediumRewards_whenScoreIsBetween70And84() = runTest {
        // Given
        val messages = listOf(
            ChatMessage("Bonjour, un cafe", isUser = true)
        )
        val rawEvaluation = BossEvaluationResult(
            task_completed = true,
            fluency_score = 75,
            target_language_percentage = 80
        )
        coEvery { repository.evaluateBossStage(any(), sampleScenario) } returns Result.success(rawEvaluation)

        // When
        val result = useCase(messages, sampleScenario)

        // Then
        assertTrue(result.isSuccess)
        val evaluation = result.getOrThrow()
        assertTrue(evaluation.task_completed)
        assertEquals(75, evaluation.fluency_score)
        assertEquals(2, evaluation.stars)
        assertEquals(10, evaluation.xp_earned)
        assertEquals(2, evaluation.coins_earned)
    }

    @Test
    fun invoke_awards1StarAndLowRewards_whenScoreIsBetween50And69() = runTest {
        // Given
        val messages = listOf(
            ChatMessage("Un cafe", isUser = true)
        )
        val rawEvaluation = BossEvaluationResult(
            task_completed = true,
            fluency_score = 55,
            target_language_percentage = 80
        )
        coEvery { repository.evaluateBossStage(any(), sampleScenario) } returns Result.success(rawEvaluation)

        // When
        val result = useCase(messages, sampleScenario)

        // Then
        assertTrue(result.isSuccess)
        val evaluation = result.getOrThrow()
        assertTrue(evaluation.task_completed)
        assertEquals(55, evaluation.fluency_score)
        assertEquals(1, evaluation.stars)
        assertEquals(5, evaluation.xp_earned)
        assertEquals(1, evaluation.coins_earned)
    }

    @Test
    fun invoke_capsScoreAndAwardsZeroRewards_whenTargetLanguagePercentageIsBelow50() = runTest {
        // Given
        val messages = listOf(
            ChatMessage("Hello please give me coffee", isUser = true)
        )
        val rawEvaluation = BossEvaluationResult(
            task_completed = true,
            fluency_score = 80,
            target_language_percentage = 10
        )
        coEvery { repository.evaluateBossStage(any(), sampleScenario) } returns Result.success(rawEvaluation)

        // When
        val result = useCase(messages, sampleScenario)

        // Then
        assertTrue(result.isSuccess)
        val evaluation = result.getOrThrow()
        assertFalse(evaluation.task_completed)
        assertTrue(evaluation.fluency_score <= 35)
        assertEquals(0, evaluation.stars)
        assertEquals(0, evaluation.xp_earned)
        assertEquals(0, evaluation.coins_earned)
    }

    @Test
    fun invoke_capsScoreAt50AndZeroStars_whenTaskCompletedIsFalseEvenWithHighFluency() = runTest {
        // Given
        val messages = listOf(
            ChatMessage("Bonjour", isUser = true)
        )
        val rawEvaluation = BossEvaluationResult(
            task_completed = false,
            fluency_score = 85,
            target_language_percentage = 90
        )
        coEvery { repository.evaluateBossStage(any(), sampleScenario) } returns Result.success(rawEvaluation)

        // When
        val result = useCase(messages, sampleScenario)

        // Then
        assertTrue(result.isSuccess)
        val evaluation = result.getOrThrow()
        assertFalse(evaluation.task_completed)
        assertEquals(50, evaluation.fluency_score)
        assertEquals(0, evaluation.stars)
        assertEquals(0, evaluation.xp_earned)
        assertEquals(0, evaluation.coins_earned)
    }

    @Test
    fun invoke_returnsFailureResult_whenRepositoryFails() = runTest {
        // Given
        val messages = listOf(
            ChatMessage("Bonjour", isUser = true)
        )
        val expectedException = RuntimeException("Gemini evaluation error")
        coEvery { repository.evaluateBossStage(any(), sampleScenario) } returns Result.failure(expectedException)

        // When
        val result = useCase(messages, sampleScenario)

        // Then
        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
        coVerify(exactly = 1) { repository.evaluateBossStage(any(), sampleScenario) }
    }
}
