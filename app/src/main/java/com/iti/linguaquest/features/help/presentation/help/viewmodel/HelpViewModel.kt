package com.iti.linguaquest.features.help.presentation.help.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.help.data.HelpFaqProvider
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

    private val _state = MutableStateFlow(
        HelpState(
            faqItems = HelpFaqProvider.defaultFaqs(),
            expandedFaqId = null
        )
    )
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HelpEffect>()
    val effect: SharedFlow<HelpEffect> = _effect.asSharedFlow()

    fun onIntent(intent: HelpIntent) {
        when (intent) {
            is HelpIntent.OnFaqToggled -> onFaq(intent)

            HelpIntent.OnContactSupportClicked -> emitEffect(HelpEffect.NavigateToContactSupport)

            HelpIntent.OnReportBugClicked -> emitEffect(HelpEffect.NavigateToReportBug)

            HelpIntent.OnBackClicked -> emitEffect(HelpEffect.NavigateBack)
        }
    }

    private fun onFaq(intent: HelpIntent.OnFaqToggled) {
        _state.update { current ->
            val newExpandedId = if (current.expandedFaqId == intent.faqId) null else intent.faqId
            current.copy(expandedFaqId = newExpandedId)
        }
    }

    private fun emitEffect(effect: HelpEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
