package com.iti.linguaquest.features.onBoarding.presentation.viewModel.levelViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetProficiencyLevelUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.SaveProficiencyLevelUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.SetIsFirstTimeUseCase
import com.iti.linguaquest.features.onBoarding.presentation.contract.levelContract.LevelEffect
import com.iti.linguaquest.features.onBoarding.presentation.contract.levelContract.LevelIntent
import com.iti.linguaquest.features.onBoarding.presentation.contract.levelContract.LevelState
import com.iti.linguaquest.features.onBoarding.presentation.contract.levelContract.ProficiencyLevel
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
    private val getProficiencyLevelUseCase: GetProficiencyLevelUseCase,
    private val saveProficiencyLevelUseCase: SaveProficiencyLevelUseCase,
    private val setIsFirstTimeUseCase: SetIsFirstTimeUseCase
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
            getProficiencyLevelUseCase().collectLatest { savedName ->
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
            saveProficiencyLevelUseCase(level.name)
        }
    }

    private fun onContinueClicked() {
        viewModelScope.launch {
            saveProficiencyLevelUseCase(_state.value.selectedLevel.name)
            setIsFirstTimeUseCase(false)
            _effect.emit(LevelEffect.NavigateToHome)
        }
    }
}