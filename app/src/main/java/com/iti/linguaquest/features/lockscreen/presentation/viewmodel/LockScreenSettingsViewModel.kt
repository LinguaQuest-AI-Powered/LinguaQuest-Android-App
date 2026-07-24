package com.iti.linguaquest.features.lockscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenFeatureState
import com.iti.linguaquest.features.lockscreen.domain.usecase.ClearLockScreenWordsUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.DisableLockScreenVocabularyUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.EnableLockScreenVocabularyUseCase
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.lockscreen.domain.usecase.GenerateVocabularyBatchUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.ObserveLockScreenSettingsMetadataUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.ObserveLockScreenUserPreferencesUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.ObserveLockScreenPendingOnceUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.UpdateLockScreenMetadataUseCase
import com.iti.linguaquest.features.lockscreen.worker.VocabularyWorkScheduler
import com.iti.linguaquest.features.lockscreen.notification.VocabularyNotificationManager
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenEffect
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenIntent
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LockScreenSettingsViewModel @Inject constructor(
    private val observeSettingsMetadataUseCase: ObserveLockScreenSettingsMetadataUseCase,
    private val clearWordsUseCase: ClearLockScreenWordsUseCase,
    private val updateMetadataUseCase: UpdateLockScreenMetadataUseCase,
    private val observePendingOnceUseCase: ObserveLockScreenPendingOnceUseCase,
    private val observeUserPreferencesUseCase: ObserveLockScreenUserPreferencesUseCase,
    private val enableUseCase: EnableLockScreenVocabularyUseCase,
    private val disableUseCase: DisableLockScreenVocabularyUseCase,
    private val generateUseCase: GenerateVocabularyBatchUseCase,
    private val scheduler: VocabularyWorkScheduler,
    private val notificationManager: VocabularyNotificationManager
) : ViewModel() {

    private val _state = MutableStateFlow(LockScreenState())
    val state: StateFlow<LockScreenState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LockScreenEffect>()
    val effect: SharedFlow<LockScreenEffect> = _effect.asSharedFlow()

    private var isHandlingInvalidation = false


    init {
        observeState()
    }

    fun onIntent(intent: LockScreenIntent) {
        when (intent) {
            is LockScreenIntent.ToggleFeatureClicked -> handleToggle(intent.enabled)
            is LockScreenIntent.SyncNotificationPermission -> {
                _state.update { it.copy(isNotificationPermissionGranted = intent.granted) }
            }
            is LockScreenIntent.NotificationPermissionResult -> handlePermissionResult(intent.granted)
            LockScreenIntent.ConfirmEnableClicked -> confirmEnable()
            LockScreenIntent.CancelEnableClicked -> hideConfirmDialog()
            LockScreenIntent.DisableClicked -> disableFeature()
            LockScreenIntent.RetryClicked -> retry()
            LockScreenIntent.DismissErrorClicked -> _state.update { it.copy(errorMessage = null) }
            LockScreenIntent.RefreshClicked -> refreshNow()
            LockScreenIntent.TestNotificationClicked -> testNotification()
        }
    }

    private fun observeState() {
        viewModelScope.launch {
            combine(
                observeSettingsMetadataUseCase(),
                observeUserPreferencesUseCase()
            ) { values: Array<Any?> ->
                val metadata = values[0] as com.iti.linguaquest.features.lockscreen.domain.usecase.LockScreenMetadata
                val enabled = metadata.featureEnabled
                val pendingGeneration = metadata.pendingGeneration
                val batchSize = metadata.batchSize
                val lastGenerationTime = metadata.lastGenerationTime
                val lastNativeLanguage = metadata.lastNativeLanguage
                val lastTargetLanguage = metadata.lastTargetLanguage
                val lastProficiencyLevel = metadata.lastProficiencyLevel
                val pendingCount = metadata.pendingCount
                val pendingOperationId = metadata.pendingOperationId
                
                val userPrefs = values[1] as com.iti.linguaquest.features.lockscreen.domain.usecase.LockScreenUserPreferences
                val appLanguage = userPrefs.appLanguage
                val currentTargetLanguage = userPrefs.targetLanguageName
                val currentProficiencyLevel = userPrefs.proficiencyLevel

                _state.update { current ->
                    val resolvedErrorMessage =
                        current.errorMessage?.takeUnless {
                            it == com.iti.linguaquest.R.string.lockscreen_required_inputs_error.toString() &&
                                !currentTargetLanguage.isNullOrBlank() &&
                                !currentProficiencyLevel.isNullOrBlank()
                        }

                    val derivedState = when {
                        resolvedErrorMessage != null -> LockScreenFeatureState.ERROR
                        !enabled -> LockScreenFeatureState.DISABLED
                        pendingGeneration -> LockScreenFeatureState.ENABLING
                        else -> LockScreenFeatureState.ACTIVE
                    }

                    current.copy(
                        featureState = derivedState,
                        pendingCount = pendingCount,
                        batchSize = batchSize,
                        pendingGeneration = pendingGeneration,
                        lastGenerationTime = lastGenerationTime,
                        lastNativeLanguage = lastNativeLanguage,
                        lastTargetLanguage = lastTargetLanguage,
                        lastProficiencyLevel = lastProficiencyLevel,
                        pendingOperationId = pendingOperationId,
                        currentNativeLanguage = appLanguage,
                        currentTargetLanguage = currentTargetLanguage,
                        currentProficiencyLevel = currentProficiencyLevel,
                        errorMessage = resolvedErrorMessage
                    )
                }

                maybeInvalidateForPreferenceChange(
                    enabled = enabled,
                    pendingGeneration = pendingGeneration,
                    currentNativeLanguage = appLanguage,
                    currentTargetLanguage = currentTargetLanguage,
                    currentProficiencyLevel = currentProficiencyLevel,
                    lastNativeLanguage = lastNativeLanguage,
                    lastTargetLanguage = lastTargetLanguage,
                    lastProficiencyLevel = lastProficiencyLevel
                )
            }.collect { }
        }
    }

    private fun maybeInvalidateForPreferenceChange(
        enabled: Boolean,
        pendingGeneration: Boolean,
        currentNativeLanguage: String?,
        currentTargetLanguage: String?,
        currentProficiencyLevel: String?,
        lastNativeLanguage: String?,
        lastTargetLanguage: String?,
        lastProficiencyLevel: String?
    ) {
        if (isHandlingInvalidation) return
        if (!enabled || pendingGeneration) return

        val languageChanged = lastTargetLanguage != null &&
            currentTargetLanguage != null &&
            lastTargetLanguage != currentTargetLanguage

        val levelChanged = lastProficiencyLevel != null &&
            currentProficiencyLevel != null &&
            lastProficiencyLevel != currentProficiencyLevel

        val nativeChanged = lastNativeLanguage != null &&
            currentNativeLanguage != null &&
            lastNativeLanguage != currentNativeLanguage

        if (languageChanged || levelChanged || nativeChanged) {
            viewModelScope.launch {
                isHandlingInvalidation = true
                try {
                    if (currentNativeLanguage != null && currentTargetLanguage != null && currentProficiencyLevel != null) {
                        clearWordsUseCase()
                        updateMetadataUseCase(
                            enabled = true,
                            pendingGeneration = true,
                            batchSize = 30, // Default or from user preference
                            lastNativeLanguage = currentNativeLanguage,
                            lastTargetLanguage = currentTargetLanguage,
                            lastProficiencyLevel = currentProficiencyLevel
                        )
                        scheduler.enqueueGenerationWork()
                        _state.update {
                            it.copy(
                                featureState = LockScreenFeatureState.ENABLING,
                                errorMessage = null
                            )
                        }
                    }
                } finally {
                    isHandlingInvalidation = false
                }
            }
        }
    }

    private fun handleToggle(enabled: Boolean) {
        if (!enabled) {
            onIntent(LockScreenIntent.DisableClicked)
            return
        }

        val current = _state.value
        if (!hasRequiredGenerationInputs(current)) {
            _state.update {
                it.copy(
                    featureState = LockScreenFeatureState.ERROR,
                    errorMessage = com.iti.linguaquest.R.string.lockscreen_required_inputs_error.toString()
                )
            }
            return
        }

        if (!_state.value.isNotificationPermissionGranted) {
            sendEffect(LockScreenEffect.RequestNotificationPermission)
            return
        }

        _state.update { it.copy(isConfirmDialogVisible = true, errorMessage = null) }
    }

    private fun handlePermissionResult(granted: Boolean) {
        _state.update { it.copy(isNotificationPermissionGranted = granted) }
        if (granted) {
            _state.update { it.copy(isConfirmDialogVisible = true) }
        } else {
            sendEffect(LockScreenEffect.ShowMessage(UiText.StringResource(com.iti.linguaquest.R.string.lockscreen_notification_permission_required)))
        }
    }

    private fun confirmEnable() {
        val current = _state.value
         if (current.featureState == LockScreenFeatureState.ACTIVE || current.featureState == LockScreenFeatureState.ENABLING) {
             _state.update { it.copy(isConfirmDialogVisible = false) }
            return
        }

        if (!hasRequiredGenerationInputs(current)) {
            _state.update {
                it.copy(
                    isConfirmDialogVisible = false,
                    isLoading = false,
                    featureState = LockScreenFeatureState.ERROR,
                    errorMessage = com.iti.linguaquest.R.string.lockscreen_required_inputs_error.toString()
                )
            }
            return
        }

        viewModelScope.launch {
            val operationId = current.pendingOperationId ?: UUID.randomUUID().toString()
             _state.update {
                it.copy(
                    isConfirmDialogVisible = false,
                    isLoading = true,
                    featureState = LockScreenFeatureState.ENABLING,
                    errorMessage = null,
                    pendingOperationId = operationId
                )
            }

             when (val enableResult = enableUseCase(operationId)) {
                is LinguaQuestResult.Success -> {
                     when (val generationResult = generateUseCase()) {
                    is LinguaQuestResult.Success -> {
                         updateMetadataUseCase(
                            enabled = true,
                            pendingGeneration = false,
                            operationId = null
                        )
                         scheduler.scheduleImmediateNotification()
                        scheduler.scheduleNotificationWork()
                         _state.update {
                            it.copy(
                                isLoading = false,
                                featureState = LockScreenFeatureState.ACTIVE,
                                pendingOperationId = null,
                                errorMessage = null
                            )
                        }
                        sendEffect(LockScreenEffect.ShowMessage(UiText.StringResource(com.iti.linguaquest.R.string.lockscreen_vocabulary_enabled)))
                    }

                    is LinguaQuestResult.Failure -> {
                         val shouldRetry = generationResult.error.shouldRetryAutomatically()
                        updateMetadataUseCase(
                            enabled = true,
                            pendingGeneration = true,
                            operationId = operationId
                        )
                        if (shouldRetry) {
                            scheduler.enqueueGenerationWork()
                        }
                        _state.update {
                            it.copy(
                                isLoading = false,
                                featureState = LockScreenFeatureState.ERROR,
                                errorMessage = generationResult.error.toUserMessage(shouldRetry)
                            )
                        }
                    }
                    }
                }

                is LinguaQuestResult.Failure -> {
                     _state.update {
                        it.copy(
                            isLoading = false,
                            featureState = LockScreenFeatureState.DISABLED,
                            pendingOperationId = null,
                            errorMessage = com.iti.linguaquest.R.string.lockscreen_enable_error.toString()
                        )
                    }
                }
            }
        }
    }

    private fun hideConfirmDialog() {
        _state.update { it.copy(isConfirmDialogVisible = false) }
    }

    private fun disableFeature() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, featureState = LockScreenFeatureState.DISABLING) }
            disableUseCase()
            notificationManager.cancelAll()
            scheduler.cancelAll()
            _state.update {
                it.copy(
                    isLoading = false,
                    featureState = LockScreenFeatureState.DISABLED,
                    isConfirmDialogVisible = false,
                    errorMessage = null,
                    pendingGeneration = false,
                    pendingCount = 0,
                    pendingOperationId = null
                )
            }
            sendEffect(LockScreenEffect.ShowMessage(UiText.StringResource(com.iti.linguaquest.R.string.lockscreen_vocabulary_disabled)))
        }
    }

    private fun retry() {
        viewModelScope.launch {
            val current = _state.value
            if (current.featureState == LockScreenFeatureState.DISABLED) {
                confirmEnable()
                return@launch
            }

            _state.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = generateUseCase()) {
                is LinguaQuestResult.Success -> {
                    updateMetadataUseCase(
                        enabled = true,
                        pendingGeneration = false,
                        operationId = null
                    )
                     scheduler.scheduleImmediateNotification()
                    scheduler.scheduleNotificationWork()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            featureState = LockScreenFeatureState.ACTIVE
                        )
                    }
                }

                is LinguaQuestResult.Failure -> {
                    val shouldRetry = result.error.shouldRetryAutomatically()
                    updateMetadataUseCase(
                        enabled = true,
                        pendingGeneration = true
                    )
                    if (shouldRetry) {
                        scheduler.enqueueGenerationWork()
                    }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            featureState = LockScreenFeatureState.ERROR,
                            errorMessage = result.error.toUserMessage(shouldRetry)
                        )
                    }
                }
            }
        }
    }

    private fun refreshNow() {
        viewModelScope.launch {
            scheduler.scheduleNotificationWork()
            sendEffect(LockScreenEffect.ShowMessage(UiText.StringResource(com.iti.linguaquest.R.string.lockscreen_notifications_scheduled)))
        }
    }

    private fun testNotification() {
        viewModelScope.launch {
            sendEffect(LockScreenEffect.ShowMessage(UiText.StringResource(com.iti.linguaquest.R.string.lockscreen_test_notification_scheduled)))
            kotlinx.coroutines.delay(5000)

            val pendingWord = observePendingOnceUseCase()
            if (pendingWord != null) {
                notificationManager.show(pendingWord)
            } else {
                // Create a dummy word if none exist
                val testWord = com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord(
                    id = 9999,
                    word = "Test Word",
                    translation = "كلمة اختبار",
                    exampleSentence = "This is a test sentence.",
                    status = com.iti.linguaquest.core.database.lockscreen.LockScreenWordStatus.PENDING,
                    createdAt = System.currentTimeMillis(),
                    postedAt = null,
                    openedAt = null,
                    nativeLanguage = "Arabic",
                    targetLanguage = "English",
                    proficiencyLevel = "Beginner"
                )
                notificationManager.show(testWord)
            }
        }
    }

    private fun sendEffect(effect: LockScreenEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }


      fun hasRequiredGenerationInputs(state: LockScreenState): Boolean = true

      fun LinguaQuestDataError.shouldRetryAutomatically(): Boolean {
        return when (this) {
            LinguaQuestDataError.Remote.REQUEST_TIMEOUT,
            LinguaQuestDataError.Remote.NO_INTERNET,
            LinguaQuestDataError.Remote.TOO_MANY_REQUESTS,
            LinguaQuestDataError.Remote.SERVER -> true

            LinguaQuestDataError.Remote.BAD_REQUEST,
            LinguaQuestDataError.Remote.UNAUTHORIZED,
            LinguaQuestDataError.Remote.SERIALIZATION,
            LinguaQuestDataError.Remote.EMPTY_RESULT,
            LinguaQuestDataError.Remote.UNKNOWN,
            LinguaQuestDataError.Local.NOT_FOUND,
            LinguaQuestDataError.Local.DISK_FULL,
            LinguaQuestDataError.Local.CONSTRAINT_VIOLATION,
            LinguaQuestDataError.Local.UNKNOWN,
            is LinguaQuestDataError.CustomServerMessage -> false

            else -> false
        }
    }

      fun LinguaQuestDataError.toUserMessage(shouldRetry: Boolean): String {
        return when (this) {
            LinguaQuestDataError.Local.NOT_FOUND ->
                com.iti.linguaquest.R.string.lockscreen_required_inputs_error.toString()

            LinguaQuestDataError.Remote.NO_INTERNET,
            LinguaQuestDataError.Remote.REQUEST_TIMEOUT,
            LinguaQuestDataError.Remote.TOO_MANY_REQUESTS,
            LinguaQuestDataError.Remote.SERVER ->
                if (shouldRetry) {
                    com.iti.linguaquest.R.string.lockscreen_error_no_internet_retry.toString()
                } else {
                    com.iti.linguaquest.R.string.lockscreen_error_no_internet.toString()
                }

            LinguaQuestDataError.Remote.SERIALIZATION,
            LinguaQuestDataError.Remote.EMPTY_RESULT ->
                com.iti.linguaquest.R.string.lockscreen_error_bad_format.toString()

            LinguaQuestDataError.Remote.BAD_REQUEST,
            LinguaQuestDataError.Remote.UNAUTHORIZED,
            LinguaQuestDataError.Remote.UNKNOWN,
            LinguaQuestDataError.Local.DISK_FULL,
            LinguaQuestDataError.Local.CONSTRAINT_VIOLATION,
            LinguaQuestDataError.Local.UNKNOWN ->
                com.iti.linguaquest.R.string.lockscreen_enable_error.toString()

            is LinguaQuestDataError.CustomServerMessage ->
                if (message.isNotBlank()) message else com.iti.linguaquest.R.string.lockscreen_enable_error.toString()

            else -> com.iti.linguaquest.R.string.lockscreen_enable_error.toString()
        }
    }

}
