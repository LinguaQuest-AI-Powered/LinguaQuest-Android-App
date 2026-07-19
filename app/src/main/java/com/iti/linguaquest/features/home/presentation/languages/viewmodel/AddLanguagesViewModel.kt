package com.iti.linguaquest.features.home.presentation.languages.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.features.home.domain.usecase.GetAvailableLanguagesUseCase
import com.iti.linguaquest.features.home.domain.usecase.AddLanguagesUseCase
import com.iti.linguaquest.features.home.presentation.languages.contract.AddLanguagesEffect
import com.iti.linguaquest.features.home.presentation.languages.contract.AddLanguagesIntent
import com.iti.linguaquest.features.home.presentation.languages.contract.AddLanguagesState
import com.iti.linguaquest.features.home.presentation.mapper.toUiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddLanguagesViewModel @Inject constructor(
    private val getAvailableLanguagesUseCase: GetAvailableLanguagesUseCase,
    private val addLanguagesUseCase: AddLanguagesUseCase,
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
            _state.update { it.copy(isLoading = true) }
            when (val result = getAvailableLanguagesUseCase()) {
                is LinguaQuestResult.Success -> {
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            availableLanguages = result.data.map { it.toUiItem() }
                        )
                    }
                }
                is LinguaQuestResult.Failure -> {
                    _state.update { it.copy(isLoading = false) }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = result.error.toUiText(),
                            type = SnackbarType.ERROR,
                            actionLabel = UiText.DynamicString("Retry"),
                            onAction = { loadAvailableLanguages() }
                        )
                    )
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
            AddLanguagesIntent.AddSelectedClicked -> {
                addSelectedLanguages()
            }
        }
    }

    private fun addSelectedLanguages() {
        val selectedIds = _state.value.selectedLanguageIds.toList()
        if (selectedIds.isEmpty()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = addLanguagesUseCase(selectedIds)) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    sendEffect(AddLanguagesEffect.NavigateBack)
                }
                is LinguaQuestResult.Failure -> {
                    _state.update { it.copy(isLoading = false) }
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