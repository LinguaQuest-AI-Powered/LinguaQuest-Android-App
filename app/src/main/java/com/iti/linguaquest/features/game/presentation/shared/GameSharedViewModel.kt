package com.iti.linguaquest.features.game.presentation.shared

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.iti.linguaquest.core.sharedComponents.text.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GameSharedViewModel @Inject constructor() : ViewModel() {

    private val _sharedState = MutableStateFlow(GameSharedState())
    val sharedState: StateFlow<GameSharedState> = _sharedState.asStateFlow()

    fun setWorldAndLevelId(worldId: Int, levelId: Int) {
        _sharedState.update { it.copy(worldId = worldId, levelId = levelId) }
    }

    fun setLevelId(id: Int) {
        _sharedState.update { it.copy(levelId = id) }
    }

    fun setTargetWord(word: String) {
        _sharedState.update { it.copy(targetWord = UiText.DynamicString(word)) }
    }

    fun setCapturedImage(uri: Uri?) {
        _sharedState.update { it.copy(capturedImageUri = uri) }
    }

    fun useHint() {
        _sharedState.update { it.copy(isHintUsed = true) }
    }

    fun setVerificationOutcome(outcome: VerificationOutcome) {
        _sharedState.update { it.copy(verificationOutcome = outcome) }
    }
}