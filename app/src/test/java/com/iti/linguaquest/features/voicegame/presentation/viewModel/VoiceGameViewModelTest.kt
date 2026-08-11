package com.iti.linguaquest.features.voicegame.presentation.viewModel

import app.cash.turbine.test
import com.iti.linguaquest.core.audio.domain.usecase.PlayAudioPreviewUseCase
import com.iti.linguaquest.core.audio.domain.usecase.RecordAudioUseCase
import com.iti.linguaquest.core.audio.domain.usecase.SpeakTextUseCase
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import com.iti.linguaquest.core.wallet.domain.usecase.AdjustWalletUseCase
import com.iti.linguaquest.core.wallet.domain.usecase.GetWalletUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetTargetLanguageNameUseCase
import com.iti.linguaquest.features.dailymission.domain.usecase.GetDailyMissionWordUseCase
import com.iti.linguaquest.features.voicegame.domain.model.PronunciationSentence
import com.iti.linguaquest.features.voicegame.domain.model.VoiceEvaluation
import com.iti.linguaquest.features.voicegame.domain.usecase.EvaluatePronunciationUseCase
import com.iti.linguaquest.features.voicegame.domain.usecase.GeneratePronunciationSentenceUseCase
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameEffect
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameIntent
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGamePhase
import com.iti.linguaquest.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class VoiceGameViewModelTest {


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var speakTextUseCase: SpeakTextUseCase
    private lateinit var recordAudioUseCase: RecordAudioUseCase
    private lateinit var playAudioPreviewUseCase: PlayAudioPreviewUseCase
    private lateinit var evaluatePronunciationUseCase: EvaluatePronunciationUseCase
    private lateinit var generatePronunciationSentenceUseCase: GeneratePronunciationSentenceUseCase
    private lateinit var getDailyMissionWordUseCase: GetDailyMissionWordUseCase
    private lateinit var getTargetLanguageNameUseCase: GetTargetLanguageNameUseCase
    private lateinit var observeNetworkStatusUseCase: ObserveNetworkStatusUseCase
    private lateinit var getWalletUseCase: GetWalletUseCase
    private lateinit var adjustWalletUseCase: AdjustWalletUseCase
    private lateinit var snackbarController: SnackbarController

    private lateinit var viewModel: VoiceGameViewModel

    @Before
    fun setUp() {
        speakTextUseCase = mockk(relaxed = true)
        recordAudioUseCase = mockk(relaxed = true)
        playAudioPreviewUseCase = mockk(relaxed = true)
        evaluatePronunciationUseCase = mockk()
        generatePronunciationSentenceUseCase = mockk()
        getDailyMissionWordUseCase = mockk()
        getTargetLanguageNameUseCase = mockk()
        observeNetworkStatusUseCase = mockk()
        getWalletUseCase = mockk()
        adjustWalletUseCase = mockk()
        snackbarController = mockk(relaxed = true)

        coEvery { getDailyMissionWordUseCase() } returns LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        every { getTargetLanguageNameUseCase() } returns flowOf("Spanish")
        every { observeNetworkStatusUseCase() } returns flowOf(true)
        every { getWalletUseCase() } returns flowOf(Wallet(xp = 100, coins = 50))

        viewModel = VoiceGameViewModel(
            speakTextUseCase = speakTextUseCase,
            recordAudioUseCase = recordAudioUseCase,
            playAudioPreviewUseCase = playAudioPreviewUseCase,
            evaluatePronunciationUseCase = evaluatePronunciationUseCase,
            generatePronunciationSentenceUseCase = generatePronunciationSentenceUseCase,
            getDailyMissionWordUseCase = getDailyMissionWordUseCase,
            getTargetLanguageNameUseCase = getTargetLanguageNameUseCase,
            observeNetworkStatusUseCase = observeNetworkStatusUseCase,
            getWalletUseCase = getWalletUseCase,
            adjustWalletUseCase = adjustWalletUseCase,
            snackbarController = snackbarController
        )
    }

    @Test
    fun initialState_loadsTargetLanguageCorrectly() = runTest {
        assertEquals("Spanish", viewModel.state.value.targetLanguage)
        assertEquals(VoiceGamePhase.IDLE, viewModel.state.value.phase)
    }

    @Test
    fun onIntent_Init_generatesSentenceSuccessfully() = runTest {
        val generatedSentence = PronunciationSentence(
            sentence = "Hola amigo",
            difficulty = "Easy",
            phonetic = "/ˈo.la aˈmi.ɣo/",
            translation = "Hello friend"
        )
        coEvery {
            generatePronunciationSentenceUseCase(targetLanguage = "Spanish", topic = any())
        } returns LinguaQuestResult.Success(generatedSentence)

        viewModel.onIntent(VoiceGameIntent.Init)

        assertEquals("Hola amigo", viewModel.state.value.sentence)
        assertEquals("/ˈo.la aˈmi.ɣo/", viewModel.state.value.phonetic)
        assertEquals("Hello friend", viewModel.state.value.translation)
        assertFalse(viewModel.state.value.isLoadingSentence)
    }

    @Test
    fun onIntent_Init_showsSnackbar_whenSentenceGenerationFails() = runTest {
        coEvery {
            generatePronunciationSentenceUseCase(targetLanguage = "Spanish", topic = any())
        } returns LinguaQuestResult.Failure(LinguaQuestDataError.CustomServerMessage("Generation Error"))

        viewModel.onIntent(VoiceGameIntent.Init)

        assertFalse(viewModel.state.value.isLoadingSentence)
        coVerify(exactly = 1) {
            snackbarController.sendEvent(
                match { it.type == SnackbarType.ERROR }
            )
        }
    }

    @Test
    fun onIntent_ListenClicked_triggersSpeakTextUseCase() = runTest {
        viewModel.onIntent(VoiceGameIntent.ListenClicked)

        verify(exactly = 1) { speakTextUseCase(viewModel.state.value.sentence) }
    }

    @Test
    fun onIntent_RecordClicked_emitsRequestMicPermissionEffect() = runTest {
        viewModel.effect.test {
            viewModel.onIntent(VoiceGameIntent.RecordClicked)
            val effect = awaitItem()
            assertTrue(effect is VoiceGameEffect.RequestMicPermission)
        }
    }

    @Test
    fun onIntent_MicPermissionGranted_startsRecording() = runTest {
        viewModel.onIntent(VoiceGameIntent.MicPermissionGranted)

        verify(exactly = 1) { recordAudioUseCase.start() }
        assertEquals(VoiceGamePhase.RECORDING, viewModel.state.value.phase)
        assertFalse(viewModel.state.value.isPaused)
    }

    @Test
    fun onIntent_MicPermissionDenied_sendsErrorSnackbar() = runTest {
        viewModel.onIntent(VoiceGameIntent.MicPermissionDenied)

        coVerify(exactly = 1) {
            snackbarController.sendEvent(
                match { it.type == SnackbarType.ERROR }
            )
        }
    }

    @Test
    fun onIntent_PauseAndResume_togglesRecordingState() = runTest {
        viewModel.onIntent(VoiceGameIntent.PauseClicked)

        verify(exactly = 1) { recordAudioUseCase.pause() }
        assertTrue(viewModel.state.value.isPaused)

        viewModel.onIntent(VoiceGameIntent.ResumeClicked)

        verify(exactly = 1) { recordAudioUseCase.resume() }
        assertFalse(viewModel.state.value.isPaused)
    }

    @Test
    fun onIntent_DoneClicked_preparesReviewConfirmationDialog() = runTest {
        val samplePcm = byteArrayOf(1, 2, 3)
        val mockFile = mockk<File>()
        every { recordAudioUseCase.stopAndGetPcmData() } returns samplePcm
        every { recordAudioUseCase.savePcmAsWav(samplePcm) } returns mockFile

        viewModel.onIntent(VoiceGameIntent.DoneClicked)

        assertTrue(viewModel.state.value.showConfirmationDialog)
        verify(exactly = 1) { recordAudioUseCase.stopAndGetPcmData() }
        verify(exactly = 1) { recordAudioUseCase.savePcmAsWav(samplePcm) }
    }

    @Test
    fun onIntent_CancelRecordingClicked_resetsStateToIdle() = runTest {
        viewModel.onIntent(VoiceGameIntent.CancelRecordingClicked)

        assertEquals(VoiceGamePhase.IDLE, viewModel.state.value.phase)
        assertFalse(viewModel.state.value.showConfirmationDialog)
        verify(exactly = 1) { recordAudioUseCase.discard() }
    }

    @Test
    fun onIntent_ConfirmProcessClicked_evaluatesRecordingAndNavigates_onHighRating() = runTest {
        val samplePcm = byteArrayOf(10, 20, 30)
        val mockFile = mockk<File>()
        every { recordAudioUseCase.stopAndGetPcmData() } returns samplePcm
        every { recordAudioUseCase.savePcmAsWav(samplePcm) } returns mockFile
        viewModel.onIntent(VoiceGameIntent.DoneClicked)

        val evaluation = VoiceEvaluation(
            rating = 8,
            correctWords = listOf("Hola"),
            wrongWords = emptyList(),
            advice = "Good job"
        )
        coEvery {
            evaluatePronunciationUseCase(
                targetSentence = any(),
                targetLanguage = any(),
                audioBytes = samplePcm
            )
        } returns LinguaQuestResult.Success(evaluation)
        coEvery { adjustWalletUseCase(xpDelta = 0, coinsDelta = 10) } returns LinguaQuestResult.Success(Unit)

        viewModel.effect.test {
            viewModel.onIntent(VoiceGameIntent.ConfirmProcessClicked)

            val effect = awaitItem()
            assertTrue(effect is VoiceGameEffect.NavigateToResult)
            val resultEffect = effect as VoiceGameEffect.NavigateToResult
            assertEquals(8, resultEffect.result.rating)
            assertTrue(resultEffect.result.isPassed)
            assertEquals(10, resultEffect.result.coinsAwarded)

            assertEquals(VoiceGamePhase.IDLE, viewModel.state.value.phase)
        }
    }
}
