package com.iti.linguaquest.features.lockscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.preferences.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenFeatureState
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import com.iti.linguaquest.features.lockscreen.domain.usecase.DisableLockScreenVocabularyUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.EnableLockScreenVocabularyUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.GenerateVocabularyBatchUseCase
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
    private val repository: LockScreenRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
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

    private companion object {
        const val REQUIRED_INPUTS_ERROR_MESSAGE =
            "Select a target language and proficiency level before enabling lock screen vocabulary."
    }

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
        }
    }

    private fun observeState() {
        viewModelScope.launch {
            combine(
                repository.featureEnabled,
                repository.pendingGeneration,
                repository.batchSize,
                repository.lastGenerationTime,
                repository.lastNativeLanguage,
                repository.lastTargetLanguage,
                repository.lastProficiencyLevel,
                repository.pendingCount,
                repository.pendingOperationId,
                userPreferencesRepository.appLanguage,
                userPreferencesRepository.targetLanguage,
                userPreferencesRepository.proficiencyLevel
            ) { values: Array<Any?> ->
                val enabled = values[0] as Boolean
                val pendingGeneration = values[1] as Boolean
                val batchSize = values[2] as Int
                val lastGenerationTime = values[3] as Long?
                val lastNativeLanguage = values[4] as String?
                val lastTargetLanguage = values[5] as String?
                val lastProficiencyLevel = values[6] as String?
                val pendingCount = values[7] as Int
                val pendingOperationId = values[8] as String?
                val currentNativeLanguage = values[9] as String?
                val currentTargetLanguage = values[10] as String?
                val currentProficiencyLevel = values[11] as String?

                _state.update { current ->
                    val resolvedErrorMessage =
                        current.errorMessage?.takeUnless {
                            it == REQUIRED_INPUTS_ERROR_MESSAGE &&
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
                        currentNativeLanguage = currentNativeLanguage,
                        currentTargetLanguage = currentTargetLanguage,
                        currentProficiencyLevel = currentProficiencyLevel,
                        errorMessage = resolvedErrorMessage
                    )
                }

                maybeInvalidateForPreferenceChange(
                    enabled = enabled,
                    pendingGeneration = pendingGeneration,
                    currentNativeLanguage = currentNativeLanguage,
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
                    repository.clearWords()
                    repository.updateFeatureMetadata(
                        enabled = true,
                        pendingGeneration = true,
                        lastGenerationTime = null,
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
                    errorMessage = REQUIRED_INPUTS_ERROR_MESSAGE
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
            sendEffect(LockScreenEffect.ShowMessage("Notification permission is required to enable the feature."))
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
                    errorMessage = REQUIRED_INPUTS_ERROR_MESSAGE
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
                is LinguaQuestResult.Success -> when (val generationResult = generateUseCase()) {
                    is LinguaQuestResult.Success -> {
                        repository.updateFeatureMetadata(
                            enabled = true,
                            pendingGeneration = false,
                            operationId = null
                        )
                        scheduler.scheduleNotificationWork()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                featureState = LockScreenFeatureState.ACTIVE,
                                pendingOperationId = null,
                                errorMessage = null
                            )
                        }
                        sendEffect(LockScreenEffect.ShowMessage("Lock screen vocabulary enabled."))
                    }

                    is LinguaQuestResult.Failure -> {
                        val shouldRetry = generationResult.error.shouldRetryAutomatically()
                        repository.updateFeatureMetadata(
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

                is LinguaQuestResult.Failure -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            featureState = LockScreenFeatureState.DISABLED,
                            pendingOperationId = null,
                            errorMessage = "Could not enable lock screen vocabulary."
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
            sendEffect(LockScreenEffect.ShowMessage("Lock screen vocabulary disabled."))
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
                    repository.updateFeatureMetadata(
                        enabled = true,
                        pendingGeneration = false,
                        operationId = null
                    )
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
                    repository.updateFeatureMetadata(
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
            sendEffect(LockScreenEffect.ShowMessage("Vocabulary notifications scheduled."))
        }
    }

    private fun sendEffect(effect: LockScreenEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    private fun hasRequiredGenerationInputs(state: LockScreenState): Boolean {
        return !state.currentTargetLanguage.isNullOrBlank() &&
            !state.currentProficiencyLevel.isNullOrBlank()
    }

    private fun LinguaQuestDataError.shouldRetryAutomatically(): Boolean {
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

    private fun LinguaQuestDataError.toUserMessage(shouldRetry: Boolean): String {
        return when (this) {
            LinguaQuestDataError.Local.NOT_FOUND ->
                "Select a target language and proficiency level before enabling lock screen vocabulary."

            LinguaQuestDataError.Remote.NO_INTERNET,
            LinguaQuestDataError.Remote.REQUEST_TIMEOUT,
            LinguaQuestDataError.Remote.TOO_MANY_REQUESTS,
            LinguaQuestDataError.Remote.SERVER ->
                if (shouldRetry) {
                    "We could not generate words right now. We'll retry automatically."
                } else {
                    "We could not generate words right now. Please try again later."
                }

            LinguaQuestDataError.Remote.SERIALIZATION,
            LinguaQuestDataError.Remote.EMPTY_RESULT ->
                "The AI response was not in the expected format. Please try again later."

            LinguaQuestDataError.Remote.BAD_REQUEST,
            LinguaQuestDataError.Remote.UNAUTHORIZED,
            LinguaQuestDataError.Remote.UNKNOWN,
            LinguaQuestDataError.Local.DISK_FULL,
            LinguaQuestDataError.Local.CONSTRAINT_VIOLATION,
            LinguaQuestDataError.Local.UNKNOWN ->
                "Could not enable lock screen vocabulary."

            is LinguaQuestDataError.CustomServerMessage ->
                if (message.isNotBlank()) message else "Could not enable lock screen vocabulary."

            else -> "Could not enable lock screen vocabulary."
        }
    }
}
