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
import javax.inject.Inject

@HiltViewModel
class OTPViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(OTPState())
    val state: StateFlow<OTPState> = _state.asStateFlow()

    private val _effect = Channel<OTPEffect>()
    val effect = _effect.receiveAsFlow()

    private var timerJob: Job? = null

    init {
        startTimer()
    }

    fun onIntent(intent: OTPIntent) {
        when (intent) {
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
        sendEffect(OTPEffect.NavigateToNextScreen)
    }

    private fun startTimer() {
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