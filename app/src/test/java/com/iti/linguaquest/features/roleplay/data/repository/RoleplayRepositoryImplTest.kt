package com.iti.linguaquest.features.roleplay.data.repository

import com.iti.linguaquest.core.cache.data.datasource.UserPreferencesLocalDataSource
import com.iti.linguaquest.features.roleplay.data.audio.AudioPlayer
import com.iti.linguaquest.features.roleplay.data.audio.AudioRecorder
import com.iti.linguaquest.features.roleplay.data.datasource.remote.GeminiRoleplayRemoteDataSource
import com.iti.linguaquest.features.roleplay.data.datasource.remote.LiveRoleplayRemoteDataSource
import com.iti.linguaquest.features.roleplay.domain.model.BossEvaluationResult
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RoleplayRepositoryImplTest {

    private lateinit var liveService: LiveRoleplayRemoteDataSource
    private lateinit var geminiService: GeminiRoleplayRemoteDataSource
    private lateinit var audioRecorder: AudioRecorder
    private lateinit var audioPlayer: AudioPlayer
    private lateinit var userPreferences: UserPreferencesLocalDataSource

    private lateinit var repository: RoleplayRepositoryImpl

    private val sampleScenario = BossScenario(
        id = ScenarioId.SCENARIO_CAFE_01,
        worldId = "world_1",
        bossName = "Pierre",
        roleDescription = "Barista",
        objective = "Order a cafe",
        voiceName = "Puck"
    )

    @Before
    fun setUp() {
        liveService = mockk(relaxed = true)
        geminiService = mockk()
        audioRecorder = mockk(relaxed = true)
        audioPlayer = mockk(relaxed = true)
        userPreferences = mockk()

        every { userPreferences.targetLanguageName } returns flowOf("French")
        every { userPreferences.nativeLanguageName } returns flowOf("English")
        every { audioRecorder.startRecording() } returns emptyFlow()
        every { liveService.observeServerEvents() } returns emptyFlow()

        repository = RoleplayRepositoryImpl(
            liveService = liveService,
            geminiService = geminiService,
            audioRecorder = audioRecorder,
            audioPlayer = audioPlayer,
            userPreferences = userPreferences
        )
    }

    @Test
    fun connectToBossStage_startsAudioServicesAndConnectsLiveSession() = runTest {
        // When
        repository.connectToBossStage(sampleScenario)

        // Then
        coVerify(exactly = 1) {
            liveService.connect(
                match { it.contains("Pierre") && it.contains("French") },
                "Puck"
            )
        }
        verify(exactly = 1) { audioPlayer.start() }
        verify(atLeast = 1) { audioRecorder.startRecording() }
        verify(atLeast = 1) { liveService.observeServerEvents() }
    }

    @Test
    fun evaluateBossStage_returnsSuccess_whenGeminiServiceProducesResult() = runTest {
        // Given
        val transcript = listOf("User: Bonjour", "AI: Bonjour!")
        val expectedEvaluation = BossEvaluationResult(
            task_completed = true,
            fluency_score = 88,
            feedback_message = "Tres bien"
        )
        coEvery {
            geminiService.evaluateBossStage(
                transcript = transcript,
                taskObjective = sampleScenario.objective,
                nativeLanguage = "English",
                targetLanguage = "French"
            )
        } returns expectedEvaluation

        // When
        val result = repository.evaluateBossStage(transcript, sampleScenario)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedEvaluation, result.getOrThrow())
        coVerify(exactly = 1) {
            geminiService.evaluateBossStage(
                transcript = transcript,
                taskObjective = sampleScenario.objective,
                nativeLanguage = "English",
                targetLanguage = "French"
            )
        }
    }

    @Test
    fun evaluateBossStage_returnsFailure_whenGeminiServiceReturnsNull() = runTest {
        // Given
        val transcript = listOf("User: Bonjour")
        coEvery {
            geminiService.evaluateBossStage(any(), any(), any(), any())
        } returns null

        // When
        val result = repository.evaluateBossStage(transcript, sampleScenario)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun evaluateBossStage_returnsFailure_whenGeminiServiceThrowsException() = runTest {
        // Given
        val transcript = listOf("User: Bonjour")
        coEvery {
            geminiService.evaluateBossStage(any(), any(), any(), any())
        } throws RuntimeException("Gemini quota exhausted")

        // When
        val result = repository.evaluateBossStage(transcript, sampleScenario)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun startMicrophone_resumesAudioSending() {
        // When
        repository.startMicrophone()

        // Then
        verify(exactly = 1) { audioRecorder.resumeSending() }
    }

    @Test
    fun stopMicrophone_pausesAudioSending() {
        // When
        repository.stopMicrophone()

        // Then
        verify(exactly = 1) { audioRecorder.pauseSending() }
    }

    @Test
    fun disconnect_stopsRecorderAndPlayerAndClosesLiveService() = runTest {
        // When
        repository.disconnect()

        // Then
        verify(atLeast = 1) { audioRecorder.pauseSending() }
        verify(exactly = 1) { audioRecorder.stopRecording() }
        verify(exactly = 1) { audioPlayer.stop() }
        coVerify(exactly = 1) { liveService.close() }
    }
}
