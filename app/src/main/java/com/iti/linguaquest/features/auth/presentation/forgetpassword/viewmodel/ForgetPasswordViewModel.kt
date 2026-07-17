package com.iti.linguaquest.features.auth.presentation.forgetpassword.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.utils.ValidationUtils
import com.iti.linguaquest.features.auth.domain.usecase.SendPasswordResetOtpUseCase
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
class ForgetPasswordViewModel @Inject constructor(
    private val sendPasswordResetOtpUseCase: SendPasswordResetOtpUseCase
) : ViewModel() {

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
            performSend(email)
        }
    }

    private fun performSend(email: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalErrorRes = null) }
            val result = sendPasswordResetOtpUseCase(email)
            _state.update { it.copy(isLoading = false) }
            if (result is LinguaQuestResult.Success) {
                sendEffect(ForgetPasswordEffect.SendSucceeded(email))
            } else {
                _state.update { it.copy(generalErrorRes = R.string.login_error_generic) }
            }
        }
    }

    private fun sendEffect(effect: ForgetPasswordEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}
