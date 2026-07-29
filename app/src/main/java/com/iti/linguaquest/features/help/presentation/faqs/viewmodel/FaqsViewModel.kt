package com.iti.linguaquest.features.help.presentation.faqs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.help.presentation.faqs.contract.FaqsEffect
import com.iti.linguaquest.features.help.presentation.faqs.contract.FaqsIntent
import com.iti.linguaquest.features.help.presentation.faqs.contract.FaqsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaqsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(FaqsState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<FaqsEffect>()
    val effect: SharedFlow<FaqsEffect> = _effect.asSharedFlow()

    fun onIntent(intent: FaqsIntent) {
        when (intent) {
            FaqsIntent.OnBackClicked -> emitEffect(FaqsEffect.NavigateBack)
        }
    }

    private fun emitEffect(effect: FaqsEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
