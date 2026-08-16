package com.iti.linguaquest.features.game.presentation.level.viewmodel

import app.cash.turbine.test
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.domain.model.GameCost
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import com.iti.linguaquest.core.wallet.domain.usecase.GetWalletUseCase
import com.iti.linguaquest.core.wallet.domain.usecase.RefreshWalletUseCase
import com.iti.linguaquest.features.game.domain.model.Hint
import com.iti.linguaquest.features.game.domain.usecase.ChangeWordUseCase
import com.iti.linguaquest.features.game.domain.usecase.GetHintUseCase
import com.iti.linguaquest.features.game.domain.usecase.StartLevelUseCase
import com.iti.linguaquest.features.game.presentation.level.contract.LevelEffect
import com.iti.linguaquest.features.game.presentation.level.contract.LevelIntent
import com.iti.linguaquest.util.MainDispatcherRule
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
import org.junit.Rule
import org.junit.Test

class LevelViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val startLevelUseCase: StartLevelUseCase = mockk()
    private val changeWordUseCase: ChangeWordUseCase = mockk()
    private val getHintUseCase: GetHintUseCase = mockk()
    private val refreshWalletUseCase: RefreshWalletUseCase = mockk(relaxed = true)
    private val getWalletUseCase: GetWalletUseCase = mockk()
    private val snackbarController: SnackbarController = mockk(relaxed = true)
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase = mockk()

    private lateinit var viewModel: LevelViewModel

    @Before
    fun setUp() {
        every { observeNetworkStatusUseCase() } returns flowOf(true)
        every { getWalletUseCase() } returns flowOf(Wallet(coins = 100,xp = 10))

        viewModel = LevelViewModel(
            startLevelUseCase,
            changeWordUseCase,
            getHintUseCase,
            refreshWalletUseCase,
            getWalletUseCase,
            snackbarController,
            observeNetworkStatusUseCase,
            mockk(relaxed = true)
        )
    }

    @Test
    fun init_loadsWalletCoins() = runTest {
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(100, state.coinCount)
        }
    }

    @Test
    fun loadLevelDetails_setsTargetWordDirectly_whenTargetWordIsProvided() = runTest {
        // Given
        val worldId = 1
        val levelId = 1
        val order = 1
        val targetWord = "Apple"

        // When
        viewModel.loadLevelDetails(worldId, levelId, order, targetWord)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(targetWord, state.wordToGuess)
            assertFalse(state.isLoading)
            assertTrue(state.isLevelReady)
        }
        coVerify(exactly = 0) { startLevelUseCase(any(), any()) }
    }

    @Test
    fun loadLevelDetails_callsStartLevelUseCase_whenTargetWordNotProvided() = runTest {
        // Given
        val worldId = 1
        val levelId = 1
        val order = 1
        val expectedWord = "Apple"
        coEvery { startLevelUseCase(worldId, levelId) } returns LinguaQuestResult.Success(expectedWord)

        // When
        viewModel.loadLevelDetails(worldId, levelId, order, null)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(expectedWord, state.wordToGuess)
            assertFalse(state.isLoading)
            assertTrue(state.isLevelReady)
        }
        coVerify(exactly = 1) { startLevelUseCase(worldId, levelId) }
        coVerify(exactly = 1) { refreshWalletUseCase() }
    }

    @Test
    fun loadLevelDetails_showsError_whenStartLevelUseCaseFails() = runTest {
        // Given
        val worldId = 1
        val levelId = 1
        val order = 1
        val error = LinguaQuestDataError.Remote.NO_INTERNET
        coEvery { startLevelUseCase(worldId, levelId) } returns LinguaQuestResult.Failure(error)

        // When
        viewModel.loadLevelDetails(worldId, levelId, order, null)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertFalse(state.isLevelReady)
            assertFalse(state.isChangeWordAvailable)
        }
        coVerify(exactly = 1) {
            snackbarController.sendEvent(match { it.type == SnackbarType.ERROR })
        }
    }

    @Test
    fun onIntent_sendsNavigateBackEffect_whenBackClicked() = runTest {
        // When
        viewModel.onIntent(LevelIntent.BackClicked)

        // Then
        viewModel.effects.test {
            assertEquals(LevelEffect.NavigateBack, awaitItem())
        }
    }

    @Test
    fun onIntent_sendsLaunchCameraEffect_whenOpenCameraClickedAndLevelIsReady() = runTest {
        // Given
        viewModel.loadLevelDetails(1, 1, 1, "Apple")

        // When
        viewModel.onIntent(LevelIntent.OpenCameraClicked)

        // Then
        viewModel.effects.test {
            assertEquals(LevelEffect.LaunchCamera, awaitItem())
        }
    }

    @Test
    fun onIntent_togglesBottomSheet_whenMascotTappedAndDismissed() = runTest {
        // When mascot tapped
        viewModel.onIntent(LevelIntent.MascotTapped)
        viewModel.state.test {
            assertTrue(awaitItem().isBottomSheetVisible)
        }

        // When dismissed
        viewModel.onIntent(LevelIntent.DismissBottomSheet)
        viewModel.state.test {
            assertFalse(awaitItem().isBottomSheetVisible)
        }
    }

    @Test
    fun onIntent_changesWordSuccessfully_whenConfirmChangeWordClicked() = runTest {
        // Given
        viewModel.loadLevelDetails(1, 1, 1, "Apple")
        val expectedNewWord = "Banana"
        coEvery { changeWordUseCase(1, 1) } returns LinguaQuestResult.Success(expectedNewWord)

        // When
        viewModel.onIntent(LevelIntent.ConfirmChangeWordClicked)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(expectedNewWord, state.wordToGuess)
            assertTrue(state.isChangeWordUsed)
            assertFalse(state.isChangeWordAvailable)
            assertFalse(state.isChangeWordDialogVisible)
        }
        coVerify(exactly = 1) { changeWordUseCase(1, 1) }
    }

    @Test
    fun onIntent_buysHintSuccessfully_whenGetHintClicked() = runTest {
        // Given
        viewModel.loadLevelDetails(1, 1, 1, "Apple")
        val expectedHintText = "A yellow fruit"
        val expectedCoins = 90
        val hintResult = Hint(expectedHintText, GameCost.HINT.coins, expectedCoins)
        coEvery { getHintUseCase(1, 1) } returns LinguaQuestResult.Success(hintResult)

        // When
        viewModel.onIntent(LevelIntent.GetHintClicked)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(expectedHintText, state.hintText)
            assertEquals(expectedCoins, state.coinCount)
            assertFalse(state.isBottomSheetVisible)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertTrue(effect is LevelEffect.HintRetrieved)
            assertEquals(expectedHintText, (effect as LevelEffect.HintRetrieved).hint)
        }
        coVerify(exactly = 1) { getHintUseCase(1, 1) }
    }
}
