package com.iti.linguaquest.features.roleplay.presentation.viewModel

import app.cash.turbine.test
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import com.iti.linguaquest.core.wallet.domain.usecase.AdjustWalletUseCase
import com.iti.linguaquest.core.wallet.domain.usecase.GetWalletUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetTargetLanguageNameUseCase
import com.iti.linguaquest.features.roleplay.domain.model.BossEvaluationResult
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId
import com.iti.linguaquest.features.roleplay.domain.usecase.ConnectToBossStageUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.DisconnectRoleplayUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.EvaluateBossStageUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.GetBossScenariosUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.ObserveLiveEventsUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.StartMicrophoneUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.StopMicrophoneUseCase
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayEffect
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RoleplayViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getTargetLanguageNameUseCase: GetTargetLanguageNameUseCase
    private lateinit var connectToBossStageUseCase: ConnectToBossStageUseCase
    private lateinit var evaluateBossStageUseCase: EvaluateBossStageUseCase
    private lateinit var startMicrophoneUseCase: StartMicrophoneUseCase
    private lateinit var stopMicrophoneUseCase: StopMicrophoneUseCase
    private lateinit var disconnectRoleplayUseCase: DisconnectRoleplayUseCase
    private lateinit var observeLiveEventsUseCase: ObserveLiveEventsUseCase
    private lateinit var getBossScenariosUseCase: GetBossScenariosUseCase
    private lateinit var snackbarController: SnackbarController
    private lateinit var observeNetworkStatusUseCase: ObserveNetworkStatusUseCase
    private lateinit var getWalletUseCase: GetWalletUseCase
    private lateinit var adjustWalletUseCase: AdjustWalletUseCase

    private lateinit var liveEventsFlow: MutableSharedFlow<RoleplayLiveEvent>
    private lateinit var viewModel: RoleplayViewModel

    private val sampleScenario = BossScenario(
        id = ScenarioId.SCENARIO_CAFE_01,
        worldId = "world_1",
        bossName = "Pierre",
        roleDescription = "Barista",
        objective = "Order a coffee",
        voiceName = "Puck"
    )

    private val defaultEvaluation = BossEvaluationResult(
        task_completed = false,
        fluency_score = 0
    )

    @Before
    fun setUp() {
        getTargetLanguageNameUseCase = mockk()
        connectToBossStageUseCase = mockk(relaxed = true)
        evaluateBossStageUseCase = mockk()
        startMicrophoneUseCase = mockk(relaxed = true)
        stopMicrophoneUseCase = mockk(relaxed = true)
        disconnectRoleplayUseCase = mockk(relaxed = true)
        observeLiveEventsUseCase = mockk()
        getBossScenariosUseCase = mockk()
        snackbarController = mockk(relaxed = true)
        observeNetworkStatusUseCase = mockk()
        getWalletUseCase = mockk()
        adjustWalletUseCase = mockk(relaxed = true)

        liveEventsFlow = MutableSharedFlow()

        every { getTargetLanguageNameUseCase() } returns flowOf("French")
        every { observeLiveEventsUseCase() } returns liveEventsFlow
        every { observeNetworkStatusUseCase() } returns flowOf(true)
        every { getWalletUseCase() } returns flowOf(Wallet(xp = 120, coins = 45))
        coEvery { evaluateBossStageUseCase(any(), any()) } returns Result.success(defaultEvaluation)

        viewModel = RoleplayViewModel(
            getTargetLanguageNameUseCase = getTargetLanguageNameUseCase,
            connectToBossStageUseCase = connectToBossStageUseCase,
            evaluateBossStageUseCase = evaluateBossStageUseCase,
            startMicrophoneUseCase = startMicrophoneUseCase,
            stopMicrophoneUseCase = stopMicrophoneUseCase,
            disconnectRoleplayUseCase = disconnectRoleplayUseCase,
            observeLiveEventsUseCase = observeLiveEventsUseCase,
            getBossScenariosUseCase = getBossScenariosUseCase,
            snackbarController = snackbarController,
            observeNetworkStatusUseCase = observeNetworkStatusUseCase,
            getWalletUseCase = getWalletUseCase,
            adjustWalletUseCase = adjustWalletUseCase
        )
    }

    @Test
    fun initialState_loadsTargetLanguageAndObservesFlows() = runTest {
        // Then
        assertEquals("French", viewModel.state.value.targetLanguage)
        assertFalse(viewModel.state.value.isConnected)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun onIntent_LoadBossLobby_populatesScenario_whenFound() = runTest {
        // Given
        coEvery { getBossScenariosUseCase(any()) } returns listOf(sampleScenario)

        // When
        viewModel.onIntent(RoleplayIntent.LoadBossLobby(ScenarioId.SCENARIO_CAFE_01))

        // Then
        assertEquals(sampleScenario, viewModel.state.value.currentBossScenario)
    }

    @Test
    fun onIntent_StartBossStageClicked_connectsAndStartsSession() = runTest {
        // Given
        coEvery { getBossScenariosUseCase(any()) } returns listOf(sampleScenario)
        viewModel.onIntent(RoleplayIntent.LoadBossLobby(ScenarioId.SCENARIO_CAFE_01))

        // When
        viewModel.onIntent(RoleplayIntent.StartBossStageClicked)

        // Then
        coVerify(atLeast = 1) { connectToBossStageUseCase(sampleScenario) }
    }

    @Test
    fun onIntent_StartBossStageClicked_handlesFailureAndShowsSnackbar() = runTest {
        // Given
        coEvery { getBossScenariosUseCase(any()) } returns listOf(sampleScenario)
        viewModel.onIntent(RoleplayIntent.LoadBossLobby(ScenarioId.SCENARIO_CAFE_01))
        coEvery { connectToBossStageUseCase(sampleScenario) } throws RuntimeException("Network Error")

        // When
        viewModel.onIntent(RoleplayIntent.StartBossStageClicked)

        // Then
        assertFalse(viewModel.state.value.isConnected)
        assertFalse(viewModel.state.value.isLoading)
        assertNotNull(viewModel.state.value.error)
        coVerify(exactly = 1) {
            snackbarController.sendEvent(match { it.type == SnackbarType.ERROR })
        }
    }

    @Test
    fun onIntent_RecordClicked_startsMicrophone_whenAiIsNotSpeaking() = runTest {
        // When
        viewModel.onIntent(RoleplayIntent.RecordClicked)

        // Then
        verify(exactly = 1) { startMicrophoneUseCase() }
        assertTrue(viewModel.state.value.isUserSpeaking)
        assertFalse(viewModel.state.value.isAiThinking)
    }

    @Test
    fun onIntent_StopRecordingClicked_stopsMicrophoneAndEntersThinking() = runTest {
        // Given
        viewModel.onIntent(RoleplayIntent.RecordClicked)

        // When
        viewModel.onIntent(RoleplayIntent.StopRecordingClicked)

        // Then
        verify(exactly = 1) { stopMicrophoneUseCase() }
        assertFalse(viewModel.state.value.isUserSpeaking)
        assertTrue(viewModel.state.value.isAiThinking)
    }

    @Test
    fun onIntent_FinishStageClicked_evaluatesAndAwardsRewards_onSuccess() = runTest {
        // Given
        coEvery { getBossScenariosUseCase(any()) } returns listOf(sampleScenario)
        viewModel.onIntent(RoleplayIntent.LoadBossLobby(ScenarioId.SCENARIO_CAFE_01))
        val evaluation = BossEvaluationResult(
            task_completed = true,
            fluency_score = 90,
            stars = 3,
            xp_earned = 200,
            coins_earned = 75
        )
        coEvery { evaluateBossStageUseCase(any(), sampleScenario) } returns Result.success(evaluation)
        coEvery { adjustWalletUseCase(xpDelta = 200, coinsDelta = 75) } returns LinguaQuestResult.Success(Unit)

        // When
        viewModel.onIntent(RoleplayIntent.FinishStageClicked)

        // Then
        coVerify(atLeast = 1) { evaluateBossStageUseCase(any(), sampleScenario) }
        coVerify(atLeast = 1) { adjustWalletUseCase(xpDelta = 200, coinsDelta = 75) }
        assertEquals(evaluation, viewModel.state.value.assessmentResult)
        assertFalse(viewModel.state.value.isEvaluating)
    }

    @Test
    fun onIntent_FinishStageClicked_handlesRewardFailureSnackbar() = runTest {
        // Given
        coEvery { getBossScenariosUseCase(any()) } returns listOf(sampleScenario)
        viewModel.onIntent(RoleplayIntent.LoadBossLobby(ScenarioId.SCENARIO_CAFE_01))
        val evaluation = BossEvaluationResult(
            task_completed = true,
            fluency_score = 80,
            stars = 2,
            xp_earned = 150,
            coins_earned = 50
        )
        coEvery { evaluateBossStageUseCase(any(), sampleScenario) } returns Result.success(evaluation)
        coEvery { adjustWalletUseCase(xpDelta = 150, coinsDelta = 50) } returns LinguaQuestResult.Failure(
            LinguaQuestDataError.CustomServerMessage("Failed to update wallet")
        )

        // When
        viewModel.onIntent(RoleplayIntent.FinishStageClicked)

        // Then
        coVerify(atLeast = 1) {
            snackbarController.sendEvent(match { it.type == SnackbarType.ERROR })
        }
    }

    @Test
    fun onIntent_FinishStageClicked_handlesEvaluationFailureAndNavigatesHome() = runTest {
        // Given
        coEvery { getBossScenariosUseCase(any()) } returns listOf(sampleScenario)
        viewModel.onIntent(RoleplayIntent.LoadBossLobby(ScenarioId.SCENARIO_CAFE_01))
        coEvery { evaluateBossStageUseCase(any(), sampleScenario) } returns Result.failure(
            RuntimeException("Quota exceeded")
        )

        // When
        viewModel.effect.test {
            viewModel.onIntent(RoleplayIntent.FinishStageClicked)

            // Then
            val effect = awaitItem()
            assertTrue(effect is RoleplayEffect.NavigateToHome)
            assertFalse(viewModel.state.value.isEvaluating)
            coVerify(atLeast = 1) {
                snackbarController.sendEvent(match { it.type == SnackbarType.ERROR })
            }
        }
    }

    @Test
    fun onIntent_RetryStageClicked_resetsStateAndRestartsSession() = runTest {
        // Given
        coEvery { getBossScenariosUseCase(any()) } returns listOf(sampleScenario)
        viewModel.onIntent(RoleplayIntent.LoadBossLobby(ScenarioId.SCENARIO_CAFE_01))

        // When
        viewModel.onIntent(RoleplayIntent.RetryStageClicked)

        // Then
        coVerify(atLeast = 1) { connectToBossStageUseCase(sampleScenario) }
    }

    @Test
    fun onIntent_ReturnHomeClicked_cleansUpAndNavigatesHome() = runTest {
        // When
        viewModel.effect.test {
            viewModel.onIntent(RoleplayIntent.ReturnHomeClicked)

            // Then
            val effect = awaitItem()
            assertTrue(effect is RoleplayEffect.NavigateToHome)
            assertFalse(viewModel.state.value.isConnected)
            coVerify(atLeast = 1) { disconnectRoleplayUseCase() }
        }
    }

    @Test
    fun onIntent_AdvanceToNextWorldClicked_navigatesHome() = runTest {
        // When
        viewModel.effect.test {
            viewModel.onIntent(RoleplayIntent.AdvanceToNextWorldClicked)

            // Then
            val effect = awaitItem()
            assertTrue(effect is RoleplayEffect.NavigateToHome)
        }
    }

    @Test
    fun liveEvents_transcription_mergesConsecutiveUserMessages() = runTest {
        // When
        liveEventsFlow.emit(RoleplayLiveEvent.Transcription("Bonjour", isUser = true))
        liveEventsFlow.emit(RoleplayLiveEvent.Transcription(", un cafe", isUser = true))

        // Then
        val history = viewModel.state.value.transcriptionHistory
        assertEquals(1, history.size)
        assertEquals("Bonjour, un cafe", history.first().text)
        assertTrue(history.first().isUser)
    }

    @Test
    fun liveEvents_transcription_addsNewAiMessageAndUpdatesSpeakingState() = runTest {
        // When
        liveEventsFlow.emit(RoleplayLiveEvent.Transcription("Bonjour", isUser = true))
        liveEventsFlow.emit(RoleplayLiveEvent.Transcription("Oui, avec plaisir!", isUser = false))

        // Then
        val history = viewModel.state.value.transcriptionHistory
        assertEquals(2, history.size)
        assertEquals("Oui, avec plaisir!", history[1].text)
        assertFalse(history[1].isUser)
        assertTrue(viewModel.state.value.isAiSpeaking)
        assertFalse(viewModel.state.value.isAiThinking)
    }

    @Test
    fun liveEvents_turnComplete_resetsAiSpeakingFlags() = runTest {
        // Given
        liveEventsFlow.emit(RoleplayLiveEvent.Transcription("Parfait", isUser = false))
        assertTrue(viewModel.state.value.isAiSpeaking)

        // When
        liveEventsFlow.emit(RoleplayLiveEvent.TurnComplete)

        // Then
        assertFalse(viewModel.state.value.isAiSpeaking)
        assertFalse(viewModel.state.value.isAiThinking)
    }

    @Test
    fun liveEvents_audioChunk_setsAiSpeakingTrue() = runTest {
        // When
        liveEventsFlow.emit(RoleplayLiveEvent.AudioChunk(byteArrayOf(1, 2, 3)))

        // Then
        assertTrue(viewModel.state.value.isAiSpeaking)
        assertFalse(viewModel.state.value.isAiThinking)
    }

    @Test
    fun liveEvents_error_stopsSessionAndEmitsSnackbarEffect_whenActive() = runTest {
        // Given
        coEvery { getBossScenariosUseCase(any()) } returns listOf(sampleScenario)
        viewModel.onIntent(RoleplayIntent.LoadBossLobby(ScenarioId.SCENARIO_CAFE_01))
        viewModel.onIntent(RoleplayIntent.StartBossStageClicked)

        // When
        viewModel.effect.test {
            liveEventsFlow.emit(RoleplayLiveEvent.Error("Connection lost with server"))

            // Then
            val effect = awaitItem()
            assertTrue(effect is RoleplayEffect.ShowSnackbarAndNavigateBack)
            assertFalse(viewModel.state.value.isConnected)
            coVerify(atLeast = 1) { disconnectRoleplayUseCase() }
        }
    }
}
