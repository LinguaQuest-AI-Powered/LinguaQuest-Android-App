package com.iti.linguaquest.features.lockscreen.presentation.viewmodel

import app.cash.turbine.test
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import com.iti.linguaquest.core.wallet.domain.usecase.GetWalletUseCase
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.features.lockscreen.domain.usecase.ClaimLockScreenMilestoneRewardUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.GenerateVocabularyBatchUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.GetLockScreenPostedOrOpenedWordsUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.MarkLockScreenWordOpenedUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.ObserveLockScreenPendingOnceUseCase
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenWordDetailIntent
import com.iti.linguaquest.core.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class LockScreenWordDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val markOpenedUseCase: MarkLockScreenWordOpenedUseCase = mockk(relaxed = true)
    private val generateBatchUseCase: GenerateVocabularyBatchUseCase = mockk(relaxed = true)
    private val getPostedOrOpenedWordsUseCase: GetLockScreenPostedOrOpenedWordsUseCase = mockk(relaxed = true)
    private val claimMilestoneRewardUseCase: ClaimLockScreenMilestoneRewardUseCase = mockk(relaxed = true)
    private val observePendingOnceUseCase: ObserveLockScreenPendingOnceUseCase = mockk(relaxed = true)
    private val getWalletUseCase: GetWalletUseCase = mockk(relaxed = true)
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase = mockk(relaxed = true)

    private fun createViewModel(): LockScreenWordDetailViewModel {
        every { observeNetworkStatusUseCase() } returns flowOf(true)
        every { getWalletUseCase() } returns flowOf(Wallet(100, 50))
        return LockScreenWordDetailViewModel(
            markOpenedUseCase,
            generateBatchUseCase,
            getPostedOrOpenedWordsUseCase,
            claimMilestoneRewardUseCase,
            observePendingOnceUseCase,
            getWalletUseCase,
            observeNetworkStatusUseCase
        )
    }

    private val mockWord = LockScreenWord(
        id = 1,
        word = "test",
        translation = "test",
        exampleSentence = "ex",
        difficulty = "Easy",
        meaning = "test meaning",
        status = com.iti.linguaquest.core.database.lockscreen.LockScreenWordStatus.OPENED,
        createdAt = 0L,
        postedAt = 0L,
        openedAt = 0L,
        nativeLanguage = "en",
        targetLanguage = "es",
        proficiencyLevel = "BEGINNER"
    )

    @Test
    fun onIntent_load_observesWordsAndClaimsMilestone() = runTest {
        // Given
        val words = List(10) { mockWord.copy(id = it) }
        every { getPostedOrOpenedWordsUseCase() } returns flowOf(words)
        coEvery { claimMilestoneRewardUseCase(10) } returns LinguaQuestResult.Success(Unit)

        val viewModel = createViewModel()

        // When
        viewModel.onIntent(LockScreenWordDetailIntent.Load)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(false, state.isLoading)
            assertEquals(words, state.words)
            assertNull(state.errorMessage)
        }
        coVerify { claimMilestoneRewardUseCase(10) }
    }

    @Test
    fun setHighlightedWordId_updatesStateAndMarksOpened() = runTest {
        // Given
        every { getPostedOrOpenedWordsUseCase() } returns flowOf(emptyList<LockScreenWord>())
        val viewModel = createViewModel()

        // When
        viewModel.onIntent(LockScreenWordDetailIntent.SetHighlightedWordId(1))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(1, state.highlightedWordId)
        }
        coVerify { markOpenedUseCase(1) }
    }

    @Test
    fun requestNewWord_whenPendingWordExists_marksOpenedAndHighlights() = runTest {
        // Given
        every { getPostedOrOpenedWordsUseCase() } returns flowOf(emptyList<LockScreenWord>())
        coEvery { observePendingOnceUseCase() } returns mockWord
        val viewModel = createViewModel()

        // When
        viewModel.onIntent(LockScreenWordDetailIntent.RequestNewWord)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(mockWord.id, state.highlightedWordId)
        }
        coVerify { markOpenedUseCase(mockWord.id) }
        coVerify(exactly = 0) { generateBatchUseCase() }
    }

    @Test
    fun requestNewWord_whenNoPendingWord_generatesBatchAndMarksOpened() = runTest {
        // Given
        every { getPostedOrOpenedWordsUseCase() } returns flowOf(emptyList<LockScreenWord>())
        coEvery { observePendingOnceUseCase() } returnsMany listOf(null, mockWord)
        coEvery { generateBatchUseCase() } returns LinguaQuestResult.Success(1)
        val viewModel = createViewModel()

        // When
        viewModel.onIntent(LockScreenWordDetailIntent.RequestNewWord)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(mockWord.id, state.highlightedWordId)
        }
        coVerify { generateBatchUseCase() }
        coVerify { markOpenedUseCase(mockWord.id) }
    }

    @Test
    fun requestNewWord_whenGenerationFails_updatesErrorMessage() = runTest {
        // Given
        every { getPostedOrOpenedWordsUseCase() } returns flowOf(emptyList<LockScreenWord>())
        coEvery { observePendingOnceUseCase() } returns null
        coEvery { generateBatchUseCase() } returns LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        val viewModel = createViewModel()

        // When
        viewModel.onIntent(LockScreenWordDetailIntent.RequestNewWord)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(false, state.isLoading)
            val errorMessage = state.errorMessage as UiText.StringResource
            assertEquals(R.string.lockscreen_error_generate_failed, errorMessage.resId)
        }
    }
}
