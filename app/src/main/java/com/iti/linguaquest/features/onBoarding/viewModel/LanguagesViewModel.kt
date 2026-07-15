package com.iti.linguaquest.features.onBoarding.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.preferences.UserPreferencesRepository
import com.iti.linguaquest.features.onBoarding.contract.LanguageOption
import com.iti.linguaquest.features.onBoarding.contract.LanguagesEffect
import com.iti.linguaquest.features.onBoarding.contract.LanguagesIntent
import com.iti.linguaquest.features.onBoarding.contract.LanguagesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguagesViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LanguagesState())
    val state: StateFlow<LanguagesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LanguagesEffect>()
    val effect: SharedFlow<LanguagesEffect> = _effect.asSharedFlow()

    fun onIntent(intent: LanguagesIntent) {
        when (intent) {
            is LanguagesIntent.SelectNativeLanguage -> selectNativeLanguage(intent.language)
            is LanguagesIntent.SelectTargetLanguage -> selectTargetLanguage(intent.language)
            LanguagesIntent.ToggleNativeDropdown -> toggleDropdown(native = true)
            LanguagesIntent.ToggleTargetDropdown -> toggleDropdown(native = false)
            LanguagesIntent.ContinueClicked -> onContinueClicked()
        }
    }

    private fun selectNativeLanguage(language: LanguageOption) {
        _state.update {
            it.copy(nativeLanguage = language.displayName, isNativeDropdownExpanded = false)
        }
    }

    private fun selectTargetLanguage(language: LanguageOption) {
        _state.update {
            it.copy(
                targetLanguage = language.displayName,
                isTargetDropdownExpanded = false,
                isContinueEnabled = true
            )
        }
    }

    private fun toggleDropdown(native: Boolean) {
        _state.update {
            if (native) it.copy(isNativeDropdownExpanded = !it.isNativeDropdownExpanded)
            else it.copy(isTargetDropdownExpanded = !it.isTargetDropdownExpanded)
        }
    }

    private fun onContinueClicked() {
        val current = _state.value
        val target = current.targetLanguage ?: return
        viewModelScope.launch {
            userPreferencesRepository.saveNativeLanguage(current.nativeLanguage)
            userPreferencesRepository.saveTargetLanguage(target)
            _effect.emit(LanguagesEffect.NavigateToLevelScreen)
        }
    }
}