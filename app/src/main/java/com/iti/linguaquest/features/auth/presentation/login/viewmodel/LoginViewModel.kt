package com.iti.linguaquest.features.auth.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.core.utils.ValidationUtils
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.usecase.LoginWithEmailUseCase
import com.iti.linguaquest.features.auth.domain.usecase.LoginWithGoogleUseCase
import com.iti.linguaquest.features.auth.presentation.login.contract.LoginEffect
import com.iti.linguaquest.features.auth.presentation.login.contract.LoginIntent
import com.iti.linguaquest.features.auth.presentation.login.contract.LoginState
import com.iti.linguaquest.features.auth.presentation.login.mapper.toMessageRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effects = Channel<LoginEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.LoginClicked -> loginWithEmail(intent.email, intent.password)

            LoginIntent.GoogleSignInClicked -> startGoogleSignIn()

            is LoginIntent.GoogleLoginSubmitted -> loginWithGoogle(intent.idToken)

            LoginIntent.GoogleSignInFailed -> handleGoogleFailure(AuthError.Unknown)

            LoginIntent.ForgetPasswordClicked -> sendEffect(LoginEffect.NavigateToForgotPassword)


            LoginIntent.SignUpClicked -> sendEffect(LoginEffect.NavigateToSignUp)

        }
    }

    private fun loginWithEmail(email: String, password: String) {

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalErrorRes = null) }
            when (val result = loginWithEmailUseCase(email, password)) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    sendEffect(LoginEffect.LoginSucceeded)
                }

                is LinguaQuestResult.Failure -> handleAuthFailure(result.error)
            }
        }
    }

    private fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalErrorRes = null) }
            when (val result = loginWithGoogleUseCase(idToken)) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isLoading = false, googleError = false) }
                    sendEffect(LoginEffect.LoginSucceeded)
                }

                is LinguaQuestResult.Failure -> handleGoogleFailure(result.error)
            }
        }
    }

    private fun handleAuthFailure(error: AuthError) {
        val emailHasError = error == AuthError.InvalidCredentials || error == AuthError.InvalidEmail
        val passwordHasError = error == AuthError.InvalidCredentials || error == AuthError.WeakPassword
        _state.update {
            it.copy(
                isLoading = false,
                generalErrorRes = null,
                emailError = emailHasError,
                emailErrorRes = if (emailHasError) error.toMessageRes() else null,
                passwordError = passwordHasError,
                passwordErrorRes = if (passwordHasError) error.toMessageRes() else null,
                googleError = false,
            )
        }

        when (error) {
            AuthError.InvalidEmail -> sendEffect(LoginEffect.ShakeEmail)
            AuthError.WeakPassword -> sendEffect(LoginEffect.ShakePassword)
            AuthError.InvalidCredentials -> {
                sendEffect(LoginEffect.ShakeEmail)
                sendEffect(LoginEffect.ShakePassword)
            }

            else -> Unit
        }
    }

    private fun handleGoogleFailure(error: AuthError) {
        _state.update {
            it.copy(
                isLoading = false,
                googleError = true,
                generalErrorRes = error.toMessageRes(),
            )
        }
        sendEffect(LoginEffect.ShakeGoogleSignIn)
    }

    private fun startGoogleSignIn() {
        _state.update {
            it.copy(
                googleError = false,
                generalErrorRes = null,
            )
        }
        sendEffect(LoginEffect.LaunchGoogleSignIn)
    }

    private fun sendEffect(effect: LoginEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}