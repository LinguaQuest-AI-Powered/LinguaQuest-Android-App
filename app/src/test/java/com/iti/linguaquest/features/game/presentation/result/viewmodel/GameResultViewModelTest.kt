package com.iti.linguaquest.features.game.presentation.result.viewmodel

import app.cash.turbine.test
import com.iti.linguaquest.R
import com.iti.linguaquest.core.appicon.usecase.LessonCompletedUseCase
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.core.wallet.domain.usecase.RefreshWalletUseCase
import com.iti.linguaquest.features.game.domain.model.Hint
import com.iti.linguaquest.features.game.domain.usecase.GetHintUseCase
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultEffect
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultIntent
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultUiState
import com.iti.linguaquest.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GameResultViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val lessonCompletedUseCase: LessonCompletedUseCase = mockk(relaxed = true)
    private val getHintUseCase: GetHintUseCase = mockk()
    private val refreshWalletUseCase: RefreshWalletUseCase = mockk(relaxed = true)
    private val snackbarController: SnackbarController = mockk(relaxed = true)

    private lateinit var viewModel: GameResultViewModel

    @Before
    fun setUp() {
        viewModel = GameResultViewModel(
            lessonCompletedUseCase,
            getHintUseCase,
            refreshWalletUseCase,
            snackbarController
        )
    }

    @Test
    fun onIntent_sendsNavigateToCameraEffect_whenRetryClicked() = runTest {
        // When
        viewModel.onIntent(GameResultIntent.RetryClicked)

        // Then
        viewModel.effect.test {
            assertEquals(GameResultEffect.NavigateToCamera, awaitItem())
        }
    }

    @Test
    fun onIntent_sendsNavigateToNextLevelEffect_whenNextLevelClicked() = runTest {
        // When
        viewModel.onIntent(GameResultIntent.NextLevelClicked)

        // Then
        viewModel.effect.test {
            assertEquals(GameResultEffect.NavigateToNextLevel, awaitItem())
        }
    }

    @Test
    fun onIntent_sendsNavigateToExitEffect_whenExitClicked() = runTest {
        // When
        viewModel.onIntent(GameResultIntent.ExitClicked)

        // Then
        viewModel.effect.test {
            assertEquals(GameResultEffect.NavigateToExit, awaitItem())
        }
    }

    @Test
    fun onIntent_buysHintAndSendsEffect_whenBuyHintClickedAndSucceeds() = runTest {
        // Given
        val worldId = 1
        val levelId = 1
        val expectedHint = "A red fruit"
        val hintResult = Hint(expectedHint, 10, 90)
        coEvery { getHintUseCase(worldId, levelId) } returns LinguaQuestResult.Success(hintResult)

        // When
        viewModel.onIntent(GameResultIntent.BuyHintClicked(worldId, levelId))

        // Then
        viewModel.effect.test {
            val effect = awaitItem()
            assertTrue(effect is GameResultEffect.ApplyHintAndRetry)
            assertEquals(expectedHint, (effect as GameResultEffect.ApplyHintAndRetry).hint)
        }
        coVerify(exactly = 1) { refreshWalletUseCase() }
    }

    @Test
    fun onIntent_showsSnackbar_whenBuyHintClickedAndFails() = runTest {
        // Given
        val worldId = 1
        val levelId = 1
        val error = LinguaQuestDataError.Remote.SERVER
        coEvery { getHintUseCase(worldId, levelId) } returns LinguaQuestResult.Failure(error)

        // When
        viewModel.onIntent(GameResultIntent.BuyHintClicked(worldId, levelId))

        // Then
        coVerify(exactly = 1) {
            snackbarController.sendEvent(match {
                it.type == SnackbarType.ERROR
            })
        }
        coVerify(exactly = 0) { refreshWalletUseCase() }
    }

    @Test
    fun setInitialResult_updatesStateAndCallsLessonCompleted_whenResultIsSuccess() = runTest {
        val successState = GameResultUiState.Success()

        // When
        viewModel.setInitialResult(successState)

        // Then
        viewModel.state.test {
            assertEquals(successState, awaitItem())
        }
        coVerify(exactly = 1) { lessonCompletedUseCase() }
    }

    @Test
    fun setInitialResult_updatesState_whenResultIsFailure() = runTest {
        // Given
        val failureState = GameResultUiState.Failure(
            reason = UiText.StringResource(R.string.general_error)
        )

        // When
        viewModel.setInitialResult(failureState)

        // Then
        viewModel.state.test {
            assertEquals(failureState, awaitItem())
        }
        coVerify(exactly = 0) { lessonCompletedUseCase() }
    }
}
