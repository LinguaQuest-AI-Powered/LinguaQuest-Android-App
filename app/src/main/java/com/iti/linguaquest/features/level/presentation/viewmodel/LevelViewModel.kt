package com.iti.linguaquest.features.level.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.level.presentation.contract.LevelEffect
import com.iti.linguaquest.features.level.presentation.contract.LevelIntent
import com.iti.linguaquest.features.level.presentation.contract.LevelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LevelViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(LevelState())
    val state = _state.asStateFlow()

    private val _effects = Channel<LevelEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun loadLevelDetails(worldId: Int, levelNumber: Int) {
        val word = if (worldId == 1 && levelNumber == 3) "PAN" else "APPLE"
        val coinCount = 1250
        val languageCode = if (worldId == 1) "es" else "en"
        
        _state.update { 
            it.copy(
                levelNumber = levelNumber,
                wordToGuess = word,
                coinCount = coinCount,
                languageCode = languageCode
            ) 
        }
    }

    fun onIntent(intent: LevelIntent) {
        when (intent) {
            LevelIntent.BackClicked -> sendEffect(LevelEffect.NavigateBack)
            LevelIntent.DismissBottomSheet -> _state.update { it.copy(isBottomSheetVisible = false) }
            LevelIntent.MascotTapped -> _state.update { it.copy(isBottomSheetVisible = true) }
            LevelIntent.OpenCameraClicked -> sendEffect(LevelEffect.LaunchCamera)
            LevelIntent.SkipClicked -> sendEffect(LevelEffect.SkipLevel)
            LevelIntent.RevealFirstLetterClicked -> deductCoinsAndHideSheet(25)
            LevelIntent.ShowCategoryClueClicked -> deductCoinsAndHideSheet(50)
            LevelIntent.SoundClicked -> sendEffect(LevelEffect.PlaySound)
        }
    }

    private fun deductCoinsAndHideSheet(cost: Int) {
        _state.update {
            val newCoins = if (it.coinCount >= cost) it.coinCount - cost else it.coinCount
            it.copy(
                coinCount = newCoins,
                isBottomSheetVisible = false
            )
        }
    }

    private fun sendEffect(effect: LevelEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}
