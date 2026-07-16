package com.iti.linguaquest.features.onBoarding.viewModel.levelViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.preferences.UserPreferencesRepository
import com.iti.linguaquest.core.sharedComponents.dialog.DialogController
import com.iti.linguaquest.core.sharedComponents.dialog.DialogUiState
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.onBoarding.contract.levelContract.LevelEffect
import com.iti.linguaquest.features.onBoarding.contract.levelContract.LevelIntent
import com.iti.linguaquest.features.onBoarding.contract.levelContract.LevelState
import com.iti.linguaquest.features.onBoarding.contract.levelContract.ProficiencyLevel
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
class LevelViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LevelState())
    val state: StateFlow<LevelState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LevelEffect>()
    val effect: SharedFlow<LevelEffect> = _effect.asSharedFlow()

    init {
        loadSavedLevel()
    }

    private fun loadSavedLevel() {
        viewModelScope.launch {
            userPreferencesRepository.proficiencyLevel.collectLatest { savedName ->
                val savedLevel = savedName?.let { name ->
                    runCatching { ProficiencyLevel.valueOf(name) }.getOrNull()
                }
                if (savedLevel != null) {
                    _state.update { it.copy(selectedLevel = savedLevel) }
                }
            }
        }
    }

    fun onIntent(intent: LevelIntent) {
        when (intent) {
            is LevelIntent.SelectLevel -> selectLevel(intent.level)
            LevelIntent.ContinueClicked -> onContinueClicked()
        }
    }

    private fun selectLevel(level: ProficiencyLevel) {
        _state.update { it.copy(selectedLevel = level) }

        viewModelScope.launch {
            userPreferencesRepository.saveProficiencyLevel(level.name)
        }
    }

    private fun onContinueClicked() {
        viewModelScope.launch {
            userPreferencesRepository.saveProficiencyLevel(_state.value.selectedLevel.name)
            _effect.emit(LevelEffect.NavigateToHome)
        }
    }
}