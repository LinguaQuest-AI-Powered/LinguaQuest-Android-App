package com.iti.linguaquest.features.home.presentation.languages.viewmodel

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
import com.iti.linguaquest.features.home.domain.usecase.GetAvailableLanguagesUseCase
import com.iti.linguaquest.features.home.domain.usecase.AddLanguagesUseCase
import com.iti.linguaquest.features.home.domain.usecase.RemoveLanguagesUseCase
import com.iti.linguaquest.features.home.presentation.languages.contract.AddLanguagesEffect
import com.iti.linguaquest.features.home.presentation.languages.contract.AddLanguagesIntent
import com.iti.linguaquest.features.home.presentation.languages.contract.AddLanguagesState
import com.iti.linguaquest.features.home.presentation.languages.contract.LanguageUiItem
import com.iti.linguaquest.features.home.presentation.mapper.toUiItem
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
class AddLanguagesViewModel @Inject constructor(
    private val getAvailableLanguagesUseCase: GetAvailableLanguagesUseCase,
    private val addLanguagesUseCase: AddLanguagesUseCase,
    private val removeLanguagesUseCase: RemoveLanguagesUseCase,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _state = MutableStateFlow(AddLanguagesState())
    val state: StateFlow<AddLanguagesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AddLanguagesEffect>()
    val effect: SharedFlow<AddLanguagesEffect> = _effect.asSharedFlow()

    init {
        loadAvailableLanguages()
    }

    private fun loadAvailableLanguages() {
        viewModelScope.launch {
            _state.update { it.copy(dataStatus = DataStatus.Loading) }
            getAvailableLanguagesUseCase().collectLatest { result ->
                when (result) {
                    is LinguaQuestResult.Success -> {
                        _state.update { state ->
                            state.copy(
                                dataStatus = DataStatus.Loaded,
                                availableLanguages = result.data.map { it.toUiItem() }
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
                                    onAction = { loadAvailableLanguages() }
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun onIntent(intent: AddLanguagesIntent) {
        when (intent) {
            AddLanguagesIntent.BackClicked -> {
                sendEffect(AddLanguagesEffect.NavigateBack)
            }
            is AddLanguagesIntent.SearchQueryChanged -> {
                _state.update { it.copy(searchQuery = intent.query) }
            }
            is AddLanguagesIntent.LanguageToggled -> {
                toggleLanguageSelection(intent.languageId)
            }
            is AddLanguagesIntent.RequestRemoveLanguage -> {
                requestRemoveLanguage(intent.language)
            }
            AddLanguagesIntent.ConfirmRemoveLanguage -> {
                confirmRemoveLanguage()
            }
            AddLanguagesIntent.DismissRemoveDialog -> {
                dismissRemoveDialog()
            }
            AddLanguagesIntent.AddSelectedClicked -> {
                addSelectedLanguages()
            }
            AddLanguagesIntent.RetryClicked -> {
                loadAvailableLanguages()
            }
        }
    }

    private fun requestRemoveLanguage(language: LanguageUiItem) {
        _state.update { it.copy(languagePendingRemoval = language) }
    }

    private fun dismissRemoveDialog() {
        _state.update { it.copy(languagePendingRemoval = null) }
    }

    private fun confirmRemoveLanguage() {
        val targetLanguage = _state.value.languagePendingRemoval ?: return
        viewModelScope.launch {
            _state.update { it.copy(isRemoving = true, languagePendingRemoval = null) }
            when (val result = removeLanguagesUseCase(listOf(targetLanguage.id))) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isRemoving = false) }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = UiText.StringResource(R.string.language_removed_success),
                            type = SnackbarType.SUCCESS
                        )
                    )
                }
                is LinguaQuestResult.Failure -> {
                    _state.update { it.copy(isRemoving = false) }
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

    private fun addSelectedLanguages() {
        val selectedIds = _state.value.selectedLanguageIds.toList()
        if (selectedIds.isEmpty()) return

        viewModelScope.launch {
            _state.update { it.copy(isAdding = true) }
            when (val result = addLanguagesUseCase(selectedIds)) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isAdding = false) }
                    sendEffect(AddLanguagesEffect.NavigateBack)
                }
                is LinguaQuestResult.Failure -> {
                    _state.update { it.copy(isAdding = false) }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = result.error.toUiText(),
                            type = SnackbarType.ERROR,
                            actionLabel = UiText.StringResource(R.string.retry),
                            onAction = { addSelectedLanguages() }
                        )
                    )
                }
            }
        }
    }

    private fun toggleLanguageSelection(languageId: Int) {
        _state.update { currentState ->
            val newSelection = currentState.selectedLanguageIds.toMutableSet()
            if (newSelection.contains(languageId)) {
                newSelection.remove(languageId)
            } else {
                newSelection.add(languageId)
            }
            currentState.copy(selectedLanguageIds = newSelection)
        }
    }

    private fun sendEffect(effect: AddLanguagesEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}