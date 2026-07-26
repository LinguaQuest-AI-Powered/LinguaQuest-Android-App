package com.iti.linguaquest.features.auth.presentation.forgetpassword.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.NetworkMonitor
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.utils.ValidationUtils
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.auth.domain.usecase.SendPasswordResetOtpUseCase
import com.iti.linguaquest.features.auth.presentation.forgetpassword.contract.ForgetPasswordEffect
import com.iti.linguaquest.features.auth.presentation.forgetpassword.contract.ForgetPasswordIntent
import com.iti.linguaquest.features.auth.presentation.forgetpassword.contract.ForgetPasswordState
import com.iti.linguaquest.features.auth.presentation.login.mapper.toMessageRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val sendPasswordResetOtpUseCase: SendPasswordResetOtpUseCase,
    private val snackbarController: SnackbarController,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _state = MutableStateFlow(ForgetPasswordState())
    val state = _state.asStateFlow()

    private val _effects = Channel<ForgetPasswordEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )
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
            when (result) {
                is LinguaQuestResult.Success -> {
                    sendEffect(ForgetPasswordEffect.SendSucceeded(email))
                }
                is LinguaQuestResult.Failure -> {
                    _state.update { it.copy(generalErrorRes = result.error.toMessageRes()) }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = UiText.StringResource(result.error.toMessageRes()),
                            type = SnackbarType.ERROR
                        )
                    )
                }
            }
        }
    }

    private fun sendEffect(effect: ForgetPasswordEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}
