package com.iti.linguaquest.features.lockscreen.presentation.viewmodel

import app.cash.turbine.test
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenFeatureMetadata
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenFeatureState
import com.iti.linguaquest.features.lockscreen.domain.usecase.ClearLockScreenWordsUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.DisableLockScreenVocabularyUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.EnableLockScreenVocabularyUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.EnqueueGenerationWorkUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.GenerateVocabularyBatchUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.GetLockScreenPendingWordUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.GetLockScreenPostedOrOpenedWordsUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.LockScreenMetadata
import com.iti.linguaquest.features.lockscreen.domain.usecase.LockScreenUserPreferences
import com.iti.linguaquest.features.lockscreen.domain.usecase.ObserveLockScreenSettingsMetadataUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.ObserveLockScreenUserPreferencesUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.ScheduleVocabularyNotificationUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.ShowTestNotificationUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.UpdateLockScreenMetadataUseCase
import com.iti.linguaquest.features.lockscreen.worker.VocabularyWorkScheduler
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenIntent
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenEffect
import com.iti.linguaquest.core.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LockScreenSettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeSettingsMetadataUseCase: ObserveLockScreenSettingsMetadataUseCase = mockk(relaxed = true)
    private val observeUserPreferencesUseCase: ObserveLockScreenUserPreferencesUseCase = mockk(relaxed = true)
    private val clearWordsUseCase: ClearLockScreenWordsUseCase = mockk(relaxed = true)
    private val updateMetadataUseCase: UpdateLockScreenMetadataUseCase = mockk(relaxed = true)
    private val enableUseCase: EnableLockScreenVocabularyUseCase = mockk(relaxed = true)
    private val disableUseCase: DisableLockScreenVocabularyUseCase = mockk(relaxed = true)
    private val generateUseCase: GenerateVocabularyBatchUseCase = mockk(relaxed = true)
    private val getPendingWordUseCase: GetLockScreenPendingWordUseCase = mockk(relaxed = true)
    private val getPostedOrOpenedWordsUseCase: GetLockScreenPostedOrOpenedWordsUseCase = mockk(relaxed = true)
    private val enqueueGenerationWorkUseCase: EnqueueGenerationWorkUseCase = mockk(relaxed = true)
    private val scheduleNotificationUseCase: ScheduleVocabularyNotificationUseCase = mockk(relaxed = true)
    private val showTestNotificationUseCase: ShowTestNotificationUseCase = mockk(relaxed = true)
    private val vocabularyWorkScheduler: VocabularyWorkScheduler = mockk(relaxed = true)
    private val snackbarController: SnackbarController = mockk(relaxed = true)

    private fun createViewModel(
        setupDefaultMetadata: Boolean = true
    ): LockScreenSettingsViewModel {
        if (setupDefaultMetadata) {
            every { observeSettingsMetadataUseCase() } returns flowOf(
                LockScreenMetadata(featureEnabled = true, pendingGeneration = false, batchSize = 10, lastGenerationTime = null, lastNativeLanguage = null, lastTargetLanguage = null, lastProficiencyLevel = null, pendingCount = 0, pendingOperationId = null)
            )
        }
        every { observeUserPreferencesUseCase() } returns flowOf(
            LockScreenUserPreferences(targetLanguageName = "Spanish", proficiencyLevel = "BEGINNER", appLanguage = "en")
        )
        return LockScreenSettingsViewModel(
            observeSettingsMetadataUseCase,
            observeUserPreferencesUseCase,
            clearWordsUseCase,
            updateMetadataUseCase,
            enableUseCase,
            disableUseCase,
            generateUseCase,
            getPendingWordUseCase,
            getPostedOrOpenedWordsUseCase,
            enqueueGenerationWorkUseCase,
            scheduleNotificationUseCase,
            showTestNotificationUseCase,
            snackbarController
        )
    }

    @Test
    fun onIntent_disableClicked_callsDisableUseCaseAndUpdatesState() = runTest {
        // Given
        val viewModel = createViewModel()
        
        // When
        viewModel.onIntent(LockScreenIntent.DisableClicked)
        
        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(LockScreenFeatureState.DISABLED, state.featureState)
            coVerify { disableUseCase() }
        }
    }
    
    @Test
    fun onIntent_toggleFeatureClicked_false_disablesFeature() = runTest {
        // Given
        val viewModel = createViewModel()
        
        // When
        viewModel.onIntent(LockScreenIntent.ToggleFeatureClicked(enabled = false))
        
        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(LockScreenFeatureState.DISABLED, state.featureState)
            coVerify { disableUseCase() }
        }
    }
    
    @Test
    fun onIntent_confirmEnableClicked_callsEnableAndGenerate() = runTest {
        // Given
        every { observeSettingsMetadataUseCase() } returns flowOf(
            LockScreenMetadata(featureEnabled = false, pendingGeneration = false, batchSize = 10, lastGenerationTime = null, lastNativeLanguage = null, lastTargetLanguage = null, lastProficiencyLevel = null, pendingCount = 0, pendingOperationId = null)
        )
        coEvery { getPendingWordUseCase() } returns flowOf(null)
        coEvery { getPostedOrOpenedWordsUseCase() } returns flowOf(emptyList<com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord>())
        coEvery { enableUseCase(any()) } returns LinguaQuestResult.Success(Unit)
        coEvery { generateUseCase() } returns LinguaQuestResult.Success(1)
        
        val viewModel = createViewModel(setupDefaultMetadata = false)
        
        // When
        viewModel.onIntent(LockScreenIntent.ConfirmEnableClicked)
        
        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(LockScreenFeatureState.ACTIVE, state.featureState)
            coVerify { enableUseCase(any()) }
            coVerify { generateUseCase() }
            coVerify { scheduleNotificationUseCase(immediate = true) }
        }
    }
}
