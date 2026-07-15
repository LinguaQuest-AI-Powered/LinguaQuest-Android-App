package com.iti.linguaquest.features.auth.login.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.core.utils.ValidationUtils
import com.iti.linguaquest.features.auth.login.domain.model.AuthError
import com.iti.linguaquest.features.auth.login.domain.usecase.ContinueAsGuestUseCase
import com.iti.linguaquest.features.auth.login.domain.usecase.LoginWithEmailUseCase
import com.iti.linguaquest.features.auth.login.domain.usecase.LoginWithGoogleUseCase
import com.iti.linguaquest.features.auth.login.presentation.contract.LoginEffect
import com.iti.linguaquest.features.auth.login.presentation.contract.LoginIntent
import com.iti.linguaquest.features.auth.login.presentation.contract.LoginState
import com.iti.linguaquest.features.auth.login.presentation.mapper.toMessageRes
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
    private val continueAsGuestUseCase: ContinueAsGuestUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effects = Channel<LoginEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> updateEmail(intent.value)

            is LoginIntent.PasswordChanged -> updatePassword(intent.value)

            LoginIntent.TogglePasswordVisibility -> togglePasswordVisibility()

            LoginIntent.LoginClicked -> loginWithEmail()

            LoginIntent.GoogleSignInClicked -> startGoogleSignIn()

            is LoginIntent.GoogleLoginSubmitted -> loginWithGoogle(intent.idToken)

            LoginIntent.GoogleSignInFailed -> handleGoogleFailure(AuthError.Unknown)

            LoginIntent.ForgetPasswordClicked -> sendEffect(LoginEffect.NavigateToForgotPassword)

            LoginIntent.ContinueAsGuestClicked -> continueAsGuest()

            LoginIntent.SignUpClicked -> sendEffect(LoginEffect.NavigateToSignUp)

        }
    }

    private fun updateEmail(value: String) {
        _state.update {
            it.copy(
                email = value,
                emailError = false,
                emailErrorRes = null,
                googleError = false,
                generalErrorRes = null,
            )
        }
    }

    private fun updatePassword(value: String) {
        _state.update {
            it.copy(
                password = value,
                passwordError = false,
                passwordErrorRes = null,
                googleError = false,
                generalErrorRes = null,
            )
        }
    }

    private fun togglePasswordVisibility() {
        _state.update {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }

    private fun loginWithEmail() {
        val currentState = state.value
        val emailIsValid = ValidationUtils.isValidEmail(currentState.email)
        val passwordIsValid = ValidationUtils.isValidPassword(currentState.password)

        if (!emailIsValid || !passwordIsValid) {
            _state.update {
                it.copy(
                    emailError = !emailIsValid,
                    emailErrorRes = if (!emailIsValid) R.string.login_error_invalid_email else null,
                    passwordError = !passwordIsValid,
                    passwordErrorRes = if (!passwordIsValid) R.string.login_error_password_required else null,
                    googleError = false,
                )
            }
            if (!emailIsValid) {
                sendEffect(LoginEffect.ShakeEmail)
            }
            if (!passwordIsValid) {
                sendEffect(LoginEffect.ShakePassword)
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalErrorRes = null) }
            when (val result = loginWithEmailUseCase(currentState.email, currentState.password)) {
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

    private fun continueAsGuest() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalErrorRes = null) }
            when (val result = continueAsGuestUseCase()) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isLoading = false, googleError = false) }
                    sendEffect(LoginEffect.LoginSucceeded)
                }

                is LinguaQuestResult.Failure -> handleAuthFailure(result.error)
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