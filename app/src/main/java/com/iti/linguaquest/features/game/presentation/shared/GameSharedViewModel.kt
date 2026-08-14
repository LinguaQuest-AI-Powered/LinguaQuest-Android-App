package com.iti.linguaquest.features.game.presentation.shared

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
 import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.sharedComponents.text.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GameSharedViewModel @Inject constructor(
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase
) : ViewModel() {

    private val _sharedState = MutableStateFlow(GameSharedState())
    val sharedState: StateFlow<GameSharedState> = _sharedState.asStateFlow()

    private val _requestChangeWordDialog = MutableStateFlow(false)
    val requestChangeWordDialog: StateFlow<Boolean> = _requestChangeWordDialog.asStateFlow()

    fun triggerChangeWordDialog() {
        _requestChangeWordDialog.value = true
    }

    fun consumeChangeWordDialogRequest() {
        _requestChangeWordDialog.value = false
    }

    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    fun setWorldAndLevelId(worldId: Int, levelId: Int) {
        _sharedState.update { it.copy(worldId = worldId, levelId = levelId) }
    }

    fun setLevelId(id: Int) {
        _sharedState.update { it.copy(levelId = id) }
    }

    fun setTargetWord(word: String) {
        _sharedState.update { it.copy(targetWord = UiText.DynamicString(word)) }
    }

    fun setTargetLanguage(language: String) {
        _sharedState.update { it.copy(targetLanguage = language) }
    }

    fun setCapturedImage(uri: Uri?) {
        _sharedState.update { it.copy(capturedImageUri = uri) }
    }

    fun useHint() {
        _sharedState.update { it.copy(isHintUsed = true) }
    }

    fun setHintText(text: String) {
        _sharedState.update { it.copy(hintText = text, isHintUsed = true) }
    }

    fun setVerificationOutcome(outcome: VerificationOutcome) {
        _sharedState.update { it.copy(verificationOutcome = outcome) }
    }

    fun clearHint() {
        _sharedState.update { it.copy(hintText = null, isHintUsed = false) }
    }
}
