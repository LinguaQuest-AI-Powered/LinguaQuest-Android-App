package com.iti.linguaquest.features.home.presentation.languages.mylanguages.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.core.sharedComponents.state.DataStatus
import com.iti.linguaquest.core.session.SessionEvent
import com.iti.linguaquest.core.session.SessionEventBus
import com.iti.linguaquest.features.home.domain.usecase.GetMyLanguagesUseCase
import com.iti.linguaquest.features.home.domain.usecase.SetActiveLanguageUseCase
import com.iti.linguaquest.features.home.domain.usecase.RemoveLanguagesUseCase
import com.iti.linguaquest.features.home.presentation.languages.mylanguages.contract.MyLanguageUiModel
import com.iti.linguaquest.features.home.presentation.languages.mylanguages.contract.MyLanguagesEffect
import com.iti.linguaquest.features.home.presentation.languages.mylanguages.contract.MyLanguagesIntent
import com.iti.linguaquest.features.home.presentation.languages.mylanguages.contract.MyLanguagesState
import com.iti.linguaquest.features.home.presentation.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyLanguagesViewModel @Inject constructor(
    private val getMyLanguagesUseCase: GetMyLanguagesUseCase,
    private val setActiveLanguageUseCase: SetActiveLanguageUseCase,
    private val removeLanguagesUseCase: RemoveLanguagesUseCase,
    private val snackbarController: SnackbarController,
    private val sessionEventBus: SessionEventBus
) : ViewModel() {

    private val _state = MutableStateFlow(MyLanguagesState())
    val state: StateFlow<MyLanguagesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MyLanguagesEffect>()
    val effect: SharedFlow<MyLanguagesEffect> = _effect.asSharedFlow()

    init {
        onIntent(MyLanguagesIntent.LoadMyLanguages)
    }

    fun onIntent(intent: MyLanguagesIntent) {
        when (intent) {
            MyLanguagesIntent.LoadMyLanguages -> loadMyLanguages()
            is MyLanguagesIntent.RequestSetActiveLanguage -> requestSetActiveLanguage(intent.language)
            MyLanguagesIntent.ConfirmSetActiveLanguage -> confirmSetActiveLanguage()
            MyLanguagesIntent.DismissSetActiveDialog -> dismissSetActiveDialog()
            is MyLanguagesIntent.RequestRemoveLanguage -> requestRemoveLanguage(intent.language)
            MyLanguagesIntent.ConfirmRemoveLanguage -> confirmRemoveLanguage()
            MyLanguagesIntent.DismissRemoveDialog -> dismissRemoveDialog()
            MyLanguagesIntent.AddNewLanguageClicked -> sendEffect(MyLanguagesEffect.NavigateToAddLanguages)
            MyLanguagesIntent.Dismiss -> sendEffect(MyLanguagesEffect.DismissSheet)
        }
    }

    private fun loadMyLanguages() {
        viewModelScope.launch {
            _state.update { it.copy(dataStatus = DataStatus.Loading) }
            getMyLanguagesUseCase().collectLatest { result ->
                when (result) {
                    is LinguaQuestResult.Success -> {
                        _state.update {
                            val newLanguages = result.data.map { lang -> lang.toUiModel() }
                            it.copy(
                                dataStatus = DataStatus.Loaded,
                                languages = newLanguages
                            )
                        }
                    }
                    is LinguaQuestResult.Failure -> {
                        val hasCache = _state.value.hasData
                        val errorUiText = result.error.toUiText()
                        
                        _state.update {
                            it.copy(
                                dataStatus = if (hasCache) DataStatus.Loaded else DataStatus.Error(errorUiText)
                            )
                        }
                        
                        if (hasCache) {
                            snackbarController.sendEvent(
                                SnackbarEvent(
                                    message = errorUiText,
                                    type = SnackbarType.ERROR,
                                    actionLabel = UiText.StringResource(R.string.retry),
                                    onAction = { loadMyLanguages() }
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun requestSetActiveLanguage(language: MyLanguageUiModel) {
        if (language.isCurrent) return
        _state.update { it.copy(languagePendingActivation = language) }
    }

    private fun dismissSetActiveDialog() {
        _state.update { it.copy(languagePendingActivation = null) }
    }

    private fun confirmSetActiveLanguage() {
        val language = _state.value.languagePendingActivation ?: return
        _state.update { it.copy(languagePendingActivation = null, isSettingActive = true) }
        
        sendEffect(MyLanguagesEffect.SwitchingLanguage)

        viewModelScope.launch {
            when (val result = setActiveLanguageUseCase(language.id)) {
                is LinguaQuestResult.Success -> {
                    _state.update { currentState ->
                        currentState.copy(
                            isSettingActive = false,
                            languages = currentState.languages.map { lang ->
                                lang.copy(isCurrent = lang.id == language.id)
                            }
                        )
                    }
                    sessionEventBus.emit(SessionEvent.LanguageChanged)
                }
                is LinguaQuestResult.Failure -> {
                    _state.update { it.copy(isSettingActive = false) }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = result.error.toUiText(),
                            type = SnackbarType.ERROR
                        )
                    )
                    sendEffect(MyLanguagesEffect.LanguageSwitchFailed(result.error.toUiText()))
                }
            }
        }
    }

    private fun requestRemoveLanguage(language: MyLanguageUiModel) {
        if (language.isCurrent) {
            viewModelScope.launch {
                snackbarController.sendEvent(
                    SnackbarEvent(
                        message = UiText.StringResource(R.string.cannot_remove_active_language),
                        type = SnackbarType.WARNING
                    )
                )
            }
            return
        }
        _state.update { it.copy(languagePendingRemoval = language) }
    }

    private fun dismissRemoveDialog() {
        _state.update { it.copy(languagePendingRemoval = null) }
    }

    private fun confirmRemoveLanguage() {
        val targetLanguage = _state.value.languagePendingRemoval ?: return
        viewModelScope.launch {
            _state.update { it.copy(isRemoving = true, removingLanguageId = targetLanguage.id, languagePendingRemoval = null) }
            when (val result = removeLanguagesUseCase(listOf(targetLanguage.id))) {
                is LinguaQuestResult.Success -> {
                    _state.update { currentState ->
                        val newLanguages = result.data.map { lang -> lang.toUiModel() }
                        currentState.copy(
                            isRemoving = false,
                            removingLanguageId = null,
                            languages = newLanguages
                        )
                    }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = UiText.StringResource(R.string.language_removed_success),
                            type = SnackbarType.SUCCESS
                        )
                    )
                }
                is LinguaQuestResult.Failure -> {
                    _state.update { it.copy(isRemoving = false, removingLanguageId = null) }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = result.error.toUiText(),
                            type = SnackbarType.ERROR
                        )
                    )
                }
            }
        }
    }

    private fun sendEffect(effect: MyLanguagesEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
