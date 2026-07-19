package com.iti.linguaquest.features.home.presentation.languages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.home.presentation.languages.contract.AddLanguagesEffect
import com.iti.linguaquest.features.home.presentation.languages.contract.AddLanguagesIntent
import com.iti.linguaquest.features.home.presentation.languages.contract.AddLanguagesState
import com.iti.linguaquest.features.home.presentation.languages.contract.LanguageUiItem
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
class AddLanguagesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(AddLanguagesState())
    val state: StateFlow<AddLanguagesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AddLanguagesEffect>()
    val effect: SharedFlow<AddLanguagesEffect> = _effect.asSharedFlow()

    init {
        // TODO: Load real data. Using dummy data for the skeleton.
        _state.update {
            it.copy(
                availableLanguages = listOf(
                    LanguageUiItem(1, "German", "🇩🇪"),
                    LanguageUiItem(2, "Italian", "🇮🇹"),
                    LanguageUiItem(3, "Japanese", "🇯🇵"),
                    LanguageUiItem(4, "Korean", "🇰🇷"),
                    LanguageUiItem(5, "Portuguese", "🇵🇹"),
                    LanguageUiItem(6, "Spanish", "🇪🇸")
                )
            )
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
                // TODO: Fire network request or DB update to add the selected languages
                sendEffect(AddLanguagesEffect.NavigateBack)
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