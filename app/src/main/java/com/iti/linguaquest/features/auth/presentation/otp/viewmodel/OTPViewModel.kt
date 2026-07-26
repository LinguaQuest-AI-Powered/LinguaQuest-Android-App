package com.iti.linguaquest.features.auth.presentation.otp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.auth.presentation.otp.contract.OTPEffect
import com.iti.linguaquest.features.auth.presentation.otp.contract.OTPIntent
import com.iti.linguaquest.features.auth.presentation.otp.contract.OTPState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.usecase.SendPasswordResetOtpUseCase
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.auth.domain.usecase.SendRegistrationOtpUseCase
import com.iti.linguaquest.features.auth.domain.usecase.VerifyEmailOtpUseCase
import com.iti.linguaquest.features.auth.domain.usecase.VerifyPasswordResetOtpUseCase
import com.iti.linguaquest.features.auth.presentation.login.mapper.toMessageRes
import javax.inject.Inject

@HiltViewModel
class OTPViewModel @Inject constructor(
    private val verifyEmailOtpUseCase: VerifyEmailOtpUseCase,
    private val verifyPasswordResetOtpUseCase: VerifyPasswordResetOtpUseCase,
    private val sendRegistrationOtpUseCase: SendRegistrationOtpUseCase,
    private val sendPasswordResetOtpUseCase: SendPasswordResetOtpUseCase,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private var email: String = ""
    private var isPasswordReset: Boolean = false

    private val _state = MutableStateFlow(OTPState())
    val state: StateFlow<OTPState> = _state.asStateFlow()

    private val _effect = Channel<OTPEffect>()
    val effect = _effect.receiveAsFlow()

    private var timerJob: Job? = null

    fun onIntent(intent: OTPIntent) {
        when (intent) {
            is OTPIntent.Initialize -> {
                this.email = intent.email
                this.isPasswordReset = intent.isPasswordReset
                startTimer()
            }
            is OTPIntent.OnOtpCodeChanged -> {
                if (intent.code.length <= 4) {
                    _state.update {
                        it.copy(
                            otpCode = intent.code,
                            isVerifyEnabled = intent.code.length == 4
                        )
                    }
                }
            }
            OTPIntent.OnVerifyClicked -> verifyOtp()
            OTPIntent.OnResendCodeClicked -> {
                startTimer()
            }
            OTPIntent.OnBackClicked -> sendEffect(OTPEffect.NavigateBack)
            OTPIntent.OnBackToLoginClicked -> sendEffect(OTPEffect.NavigateToLogin)
        }
    }

    private fun verifyOtp() {
        val otpCode = _state.value.otpCode
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            if (isPasswordReset) {
                when (val result = verifyPasswordResetOtpUseCase(email, otpCode)) {
                    is LinguaQuestResult.Success -> {
                        _state.update { it.copy(isLoading = false) }
                        sendEffect(OTPEffect.NavigateToNextScreen(result.data))
                    }
                    is LinguaQuestResult.Failure -> {
                        _state.update { it.copy(isLoading = false) }
                        snackbarController.sendEvent(
                            SnackbarEvent(
                                message = UiText.StringResource(result.error.toMessageRes()),
                                type = SnackbarType.ERROR
                            )
                        )
                    }
                }
            } else {
                when (val result = verifyEmailOtpUseCase(email, otpCode)) {
                    is LinguaQuestResult.Success -> {
                        _state.update { it.copy(isLoading = false) }
                        sendEffect(OTPEffect.NavigateToNextScreen(null))
                    }
                    is LinguaQuestResult.Failure -> {
                        _state.update { it.copy(isLoading = false) }
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
    }

    private fun startTimer() {
        viewModelScope.launch {
            if (email.isNotEmpty()) {
                if (isPasswordReset) {
                    sendPasswordResetOtpUseCase(email)
                } else {
                    sendRegistrationOtpUseCase(email)
                }
            }
        }
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            _state.update { it.copy(isTimerActive = true) }

            for (i in 90 downTo 0) {
                val minutes = (i / 60).toString().padStart(2, '0')
                val seconds = (i % 60).toString().padStart(2, '0')

                _state.update { it.copy(timerText = "$minutes:$seconds") }
                delay(1000L)
            }

            _state.update { it.copy(isTimerActive = false) }
        }
    }
    private fun sendEffect(effect: OTPEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}