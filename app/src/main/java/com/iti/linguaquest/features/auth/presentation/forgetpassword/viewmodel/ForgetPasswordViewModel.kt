package com.iti.linguaquest.features.auth.presentation.forgetpassword.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.utils.ValidationUtils
import com.iti.linguaquest.features.auth.presentation.forgetpassword.contract.ForgetPasswordEffect
import com.iti.linguaquest.features.auth.presentation.forgetpassword.contract.ForgetPasswordIntent
import com.iti.linguaquest.features.auth.presentation.forgetpassword.contract.ForgetPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ForgetPasswordState())
    val state = _state.asStateFlow()

    private val _effects = Channel<ForgetPasswordEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: ForgetPasswordIntent) {
        when (intent) {
            is ForgetPasswordIntent.SendClicked -> validateAndSend(intent.email)
            ForgetPasswordIntent.BackToLoginClicked -> sendEffect(ForgetPasswordEffect.NavigateBackToLogin)
        }
    }

    private fun validateAndSend(email: String) {
        val isValidEmail = ValidationUtils.isValidEmail(email)

        _state.update {
            it.copy(
                emailError = !isValidEmail,
                emailErrorRes = if (!isValidEmail) R.string.forget_password_error_invalid_email else null,
                generalErrorRes = null,
            )
        }

        if (!isValidEmail) {
            sendEffect(ForgetPasswordEffect.ShakeEmail)
        } else {
            performSend()
        }
    }

    private fun performSend() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalErrorRes = null) }
            // Stub network call
            _state.update { it.copy(isLoading = false) }
            sendEffect(ForgetPasswordEffect.SendSucceeded)
        }
    }

    private fun sendEffect(effect: ForgetPasswordEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}
