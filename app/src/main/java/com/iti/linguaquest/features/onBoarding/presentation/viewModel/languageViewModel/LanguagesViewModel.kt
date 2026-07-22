package com.iti.linguaquest.features.onBoarding.presentation.viewModel.languageViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetNativeLanguageUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetTargetLanguageUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.SaveNativeLanguageUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.SaveTargetLanguageUseCase
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import com.iti.linguaquest.features.auth.domain.usecase.GetAuthLanguagesUseCase
import com.iti.linguaquest.features.onBoarding.presentation.contract.languageContract.LanguagesEffect
import com.iti.linguaquest.features.onBoarding.presentation.contract.languageContract.LanguagesIntent
import com.iti.linguaquest.features.onBoarding.presentation.contract.languageContract.LanguagesState
import com.iti.linguaquest.core.result.LinguaQuestResult
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
    private val getNativeLanguageUseCase: GetNativeLanguageUseCase,
    private val getTargetLanguageUseCase: GetTargetLanguageUseCase,
    private val saveNativeLanguageUseCase: SaveNativeLanguageUseCase,
    private val saveTargetLanguageUseCase: SaveTargetLanguageUseCase,
    private val getAuthLanguagesUseCase: GetAuthLanguagesUseCase
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
            _state.update { it.copy(isLoading = true) }
            val result = getAuthLanguagesUseCase()
            if (result is LinguaQuestResult.Success) {
                val available = result.data
                _state.update { it.copy(availableLanguages = available, isLoading = false) }

                combine(
                    getNativeLanguageUseCase(),
                    getTargetLanguageUseCase()
                ) { savedNative, savedTarget -> savedNative to savedTarget }
                    .collectLatest { (savedNativeId, savedTargetId) ->
                        val nativeOpt = available.find { it.id == savedNativeId }
                            ?: available.find { it.name == "English" } ?: available.firstOrNull()
                        val targetOpt = available.find { it.id == savedTargetId }

                        _state.update {
                            it.copy(
                                nativeLanguage = nativeOpt,
                                targetLanguage = targetOpt,
                                isContinueEnabled = targetOpt != null
                            )
                        }
                    }
            } else {
                _state.update { it.copy(isLoading = false) } // Add error handling if needed
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
            it.copy(nativeLanguage = language, isNativeDropdownExpanded = false)
        }
    }

    private fun selectTargetLanguage(language: LanguageOption) {
        _state.update {
            it.copy(
                targetLanguage = language,
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
        val native = current.nativeLanguage ?: return
        viewModelScope.launch {
            saveNativeLanguageUseCase(native.id, native.name)
            saveTargetLanguageUseCase(target.id, target.name)
            _effect.emit(LanguagesEffect.NavigateToLevelScreen)
        }
    }
}