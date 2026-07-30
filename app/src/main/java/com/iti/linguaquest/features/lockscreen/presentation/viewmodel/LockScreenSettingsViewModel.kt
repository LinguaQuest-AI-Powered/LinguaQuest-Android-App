package com.iti.linguaquest.features.lockscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenFeatureState
import com.iti.linguaquest.features.lockscreen.domain.usecase.ClearLockScreenWordsUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.DisableLockScreenVocabularyUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.EnableLockScreenVocabularyUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.EnqueueGenerationWorkUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.GenerateVocabularyBatchUseCase
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenFeatureMetadata
import com.iti.linguaquest.features.lockscreen.domain.usecase.LockScreenMetadata
import com.iti.linguaquest.features.lockscreen.domain.usecase.LockScreenUserPreferences
import com.iti.linguaquest.features.lockscreen.domain.usecase.ObserveLockScreenSettingsMetadataUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.ObserveLockScreenUserPreferencesUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.ScheduleVocabularyNotificationUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.ShowTestNotificationUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.UpdateLockScreenMetadataUseCase
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenEffect
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenIntent
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenState
import com.iti.linguaquest.features.lockscreen.presentation.mapper.shouldRetryAutomatically
import com.iti.linguaquest.features.lockscreen.presentation.mapper.toUiText
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
    private val observeUserPreferencesUseCase: ObserveLockScreenUserPreferencesUseCase,
    private val clearWordsUseCase: ClearLockScreenWordsUseCase,
    private val updateMetadataUseCase: UpdateLockScreenMetadataUseCase,
    private val enableUseCase: EnableLockScreenVocabularyUseCase,
    private val disableUseCase: DisableLockScreenVocabularyUseCase,
    private val generateUseCase: GenerateVocabularyBatchUseCase,
    private val enqueueGenerationWorkUseCase: EnqueueGenerationWorkUseCase,
    private val scheduleNotificationUseCase: ScheduleVocabularyNotificationUseCase,
    private val showTestNotificationUseCase: ShowTestNotificationUseCase,
    private val snackbarController: SnackbarController
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
                val metadata = values[0] as LockScreenMetadata
                val userPrefs = values[1] as LockScreenUserPreferences

                val enabled = metadata.featureEnabled
                val pendingGeneration = metadata.pendingGeneration
                val appLanguage = userPrefs.appLanguage
                val currentTargetLanguage = userPrefs.targetLanguageName
                val currentProficiencyLevel = userPrefs.proficiencyLevel

                _state.update { current ->
                     val isRequiredInputError = current.errorMessage is UiText.StringResource &&
                            current.errorMessage .resId == R.string.lockscreen_required_inputs_error

                    val resolvedErrorMessage = current.errorMessage?.takeUnless {
                        isRequiredInputError &&
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
                        pendingCount = metadata.pendingCount,
                        batchSize = metadata.batchSize,
                        pendingGeneration = pendingGeneration,
                        lastGenerationTime = metadata.lastGenerationTime,
                        lastNativeLanguage = metadata.lastNativeLanguage,
                        lastTargetLanguage = metadata.lastTargetLanguage,
                        lastProficiencyLevel = metadata.lastProficiencyLevel,
                        pendingOperationId = metadata.pendingOperationId,
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
                    lastNativeLanguage = metadata.lastNativeLanguage,
                    lastTargetLanguage = metadata.lastTargetLanguage,
                    lastProficiencyLevel = metadata.lastProficiencyLevel
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
        if (isHandlingInvalidation || !enabled || pendingGeneration) return

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
                            LockScreenFeatureMetadata(
                                enabled = true,
                                pendingGeneration = true,
                                batchSize = 30,
                                lastNativeLanguage = currentNativeLanguage,
                                lastTargetLanguage = currentTargetLanguage,
                                lastProficiencyLevel = currentProficiencyLevel
                            )
                        )
                        enqueueGenerationWorkUseCase()
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
                    errorMessage = UiText.StringResource(R.string.lockscreen_required_inputs_error)
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
            showMessage(UiText.StringResource(R.string.lockscreen_notification_permission_required))
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
                    errorMessage = UiText.StringResource(R.string.lockscreen_required_inputs_error)
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
                                LockScreenFeatureMetadata(
                                    enabled = true,
                                    pendingGeneration = false,
                                    operationId = null
                                )
                            )
                            scheduleNotificationUseCase(immediate = true)
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    featureState = LockScreenFeatureState.ACTIVE,
                                    pendingOperationId = null,
                                    errorMessage = null
                                )
                            }
                            showMessage(UiText.StringResource(R.string.lockscreen_vocabulary_enabled))
                        }

                        is LinguaQuestResult.Failure -> {
                            val shouldRetry = generationResult.error.shouldRetryAutomatically()
                            updateMetadataUseCase(
                                LockScreenFeatureMetadata(
                                    enabled = true,
                                    pendingGeneration = true,
                                    operationId = operationId
                                )
                            )
                            if (shouldRetry) {
                                enqueueGenerationWorkUseCase()
                            }
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    featureState = LockScreenFeatureState.ERROR,
                                    errorMessage = generationResult.error.toUiText(shouldRetry)
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
                            errorMessage = UiText.StringResource(R.string.lockscreen_enable_error)
                        )
                    }
                    showMessage(enableResult.error.toUiText(), SnackbarType.ERROR)
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
            showMessage(UiText.StringResource(R.string.lockscreen_vocabulary_disabled))
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
                        LockScreenFeatureMetadata(
                            enabled = true,
                            pendingGeneration = false,
                            operationId = null
                        )
                    )
                    scheduleNotificationUseCase(immediate = true)
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
                        LockScreenFeatureMetadata(
                            enabled = true,
                            pendingGeneration = true
                        )
                    )
                    if (shouldRetry) {
                        enqueueGenerationWorkUseCase()
                    }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            featureState = LockScreenFeatureState.ERROR,
                            errorMessage = result.error.toUiText(shouldRetry)
                        )
                    }
                }
            }
        }
    }

    private fun refreshNow() {
        viewModelScope.launch {
            scheduleNotificationUseCase(immediate = false)
            showMessage(UiText.StringResource(R.string.lockscreen_notifications_scheduled))
        }
    }

    private fun testNotification() {
        viewModelScope.launch {
            showMessage(UiText.StringResource(R.string.lockscreen_test_notification_scheduled))
            showTestNotificationUseCase()
        }
    }

    private fun sendEffect(effect: LockScreenEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    private fun showMessage(text: UiText, type: SnackbarType = SnackbarType.INFO) {
        viewModelScope.launch {
            snackbarController.sendEvent(SnackbarEvent(message = text, type = type))
        }
    }

    private fun hasRequiredGenerationInputs(state: LockScreenState): Boolean {
        return !state.currentTargetLanguage.isNullOrBlank() && !state.currentProficiencyLevel.isNullOrBlank()
    }
}