package com.iti.linguaquest.features.help.presentation.help.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.help.data.HelpTopic
import com.iti.linguaquest.features.help.presentation.help.contract.HelpEffect
import com.iti.linguaquest.features.help.presentation.help.contract.HelpIntent
import com.iti.linguaquest.features.help.presentation.help.contract.HelpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(HelpState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HelpEffect>()
    val effect: SharedFlow<HelpEffect> = _effect.asSharedFlow()

    fun onIntent(intent: HelpIntent) {
        when (intent) {
            is HelpIntent.OnTopicSelected -> {
                when (intent.topic) {
                    HelpTopic.FAQS -> emitEffect(HelpEffect.NavigateToFaqs)
                    HelpTopic.CONTACT_US -> emitEffect(HelpEffect.NavigateToContactUs)
                    HelpTopic.USER_GUIDE -> emitEffect(HelpEffect.NavigateToUserGuide)
                }
            }
            HelpIntent.OnBackClicked -> emitEffect(HelpEffect.NavigateBack)
        }
    }

    private fun emitEffect(effect: HelpEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
