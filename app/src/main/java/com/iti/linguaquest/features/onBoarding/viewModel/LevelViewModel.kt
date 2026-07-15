package com.iti.linguaquest.features.onBoarding.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.preferences.UserPreferencesRepository
import com.iti.linguaquest.features.onBoarding.contract.LevelEffect
import com.iti.linguaquest.features.onBoarding.contract.LevelIntent
import com.iti.linguaquest.features.onBoarding.contract.LevelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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

    fun onIntent(intent: LevelIntent) {
        when (intent) {
            is LevelIntent.SelectLevel -> _state.update { it.copy(selectedLevel = intent.level) }
            LevelIntent.ContinueClicked -> onContinueClicked()
        }
    }

    private fun onContinueClicked() {
        viewModelScope.launch {
            userPreferencesRepository.saveProficiencyLevel(_state.value.selectedLevel.name)
            _effect.emit(LevelEffect.NavigateToHome)
        }
    }
}