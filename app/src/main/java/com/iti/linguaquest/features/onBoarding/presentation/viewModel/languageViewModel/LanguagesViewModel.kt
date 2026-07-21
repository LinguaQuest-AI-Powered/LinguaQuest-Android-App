package com.iti.linguaquest.features.onBoarding.presentation.viewModel.languageViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetAppLanguageUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetTargetLanguageUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.SaveAppLanguageUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.SaveTargetLanguageUseCase
import com.iti.linguaquest.features.onBoarding.presentation.contract.languageContract.LanguageOption
import com.iti.linguaquest.features.onBoarding.presentation.contract.languageContract.LanguagesEffect
import com.iti.linguaquest.features.onBoarding.presentation.contract.languageContract.LanguagesIntent
import com.iti.linguaquest.features.onBoarding.presentation.contract.languageContract.LanguagesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguagesViewModel @Inject constructor(
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val getTargetLanguageUseCase: GetTargetLanguageUseCase,
    private val saveAppLanguageUseCase: SaveAppLanguageUseCase,
    private val saveTargetLanguageUseCase: SaveTargetLanguageUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LanguagesState())
    val state: StateFlow<LanguagesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LanguagesEffect>()
    val effect: SharedFlow<LanguagesEffect> = _effect.asSharedFlow()

    init {
        loadSavedLanguages()
    }

    private fun loadSavedLanguages() {
        viewModelScope.launch {
            combine(
                getAppLanguageUseCase(),
                getTargetLanguageUseCase()
            ) { savedNative, savedTarget -> savedNative to savedTarget }
                .collectLatest { (savedNative, savedTarget) ->
                    _state.update {
                        it.copy(
                            nativeLanguage = savedNative ?: it.nativeLanguage,
                            targetLanguage = savedTarget,
                            isContinueEnabled = savedTarget != null
                        )
                    }
                }
        }
    }

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
        viewModelScope.launch {
            saveAppLanguageUseCase(language.displayName)
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
        viewModelScope.launch {
            saveTargetLanguageUseCase(language.displayName)
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
            saveAppLanguageUseCase(current.nativeLanguage)
            saveTargetLanguageUseCase(target)
            _effect.emit(LanguagesEffect.NavigateToLevelScreen)
        }
    }
}