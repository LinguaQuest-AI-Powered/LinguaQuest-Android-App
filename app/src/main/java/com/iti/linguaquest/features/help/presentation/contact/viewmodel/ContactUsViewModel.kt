package com.iti.linguaquest.features.help.presentation.contact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.help.presentation.contact.contract.ContactUsEffect
import com.iti.linguaquest.features.help.presentation.contact.contract.ContactUsIntent
import com.iti.linguaquest.features.help.presentation.contact.contract.ContactUsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactUsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ContactUsState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ContactUsEffect>()
    val effect: SharedFlow<ContactUsEffect> = _effect.asSharedFlow()

    fun onIntent(intent: ContactUsIntent) {
        when (intent) {
            ContactUsIntent.OnBackClicked -> emitEffect(ContactUsEffect.NavigateBack)
        }
    }

    private fun emitEffect(effect: ContactUsEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
