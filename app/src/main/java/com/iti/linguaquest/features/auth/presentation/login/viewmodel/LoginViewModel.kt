package com.iti.linguaquest.features.auth.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.core.utils.ValidationUtils
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.usecase.LoginUserUseCase
import com.iti.linguaquest.features.auth.domain.usecase.SignInWithGoogleUseCase
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
    private val loginUserUseCase: LoginUserUseCase,
    private val loginWithGoogleUseCase: SignInWithGoogleUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effects = Channel<LoginEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> {
                _state.update { it.copy(email = intent.email, emailError = false, emailErrorRes = null).withUpdatedImage() }
            }
            is LoginIntent.PasswordChanged -> {
                _state.update { it.copy(password = intent.password, passwordError = false, passwordErrorRes = null).withUpdatedImage() }
            }
            LoginIntent.LoginClicked -> handleLoginClicked()

            LoginIntent.GoogleSignInClicked -> startGoogleSignIn()

            is LoginIntent.GoogleLoginSubmitted -> loginWithGoogle(intent.idToken)

            LoginIntent.GoogleSignInFailed -> handleGoogleFailure(AuthError.Unknown)

            LoginIntent.ForgetPasswordClicked -> sendEffect(LoginEffect.NavigateToForgotPassword)


            LoginIntent.SignUpClicked -> sendEffect(LoginEffect.NavigateToSignUp)

        }
    }

    private fun handleLoginClicked() {
        val email = state.value.email
        val password = state.value.password
        val emailValid = ValidationUtils.isValidEmail(email)
        val passwordValid = ValidationUtils.isValidPassword(password)
        
        if (!emailValid) {
            _state.update { it.copy(
                emailError = true, 
                emailErrorRes = if (email.isBlank()) R.string.login_error_email_required else R.string.login_error_invalid_email
            ).withUpdatedImage() }
            sendEffect(LoginEffect.ShakeEmail)
        }
        if (!passwordValid) {
            _state.update { it.copy(
                passwordError = true,
                passwordErrorRes = if (password.isBlank()) R.string.login_error_password_required else R.string.login_error_weak_password
            ).withUpdatedImage() }
            sendEffect(LoginEffect.ShakePassword)
        }
        
        if (emailValid && passwordValid) {
            loginWithEmail(email, password)
        }
    }

    private fun loginWithEmail(email: String, password: String) {

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalErrorRes = null) }
            when (val result = loginUserUseCase(email, password)) {
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
            ).withUpdatedImage()
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
            ).withUpdatedImage()
        }
        sendEffect(LoginEffect.ShakeGoogleSignIn)
    }

    private fun startGoogleSignIn() {
        _state.update {
            it.copy(
                googleError = false,
                generalErrorRes = null,
            ).withUpdatedImage()
        }
        sendEffect(LoginEffect.LaunchGoogleSignIn)
    }

    private fun sendEffect(effect: LoginEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }

    private fun LoginState.withUpdatedImage(): LoginState {
        val newImage = when {
            emailError || passwordError || generalErrorRes != null -> R.drawable.lingo_error
            email.isNotBlank() || password.isNotBlank() -> R.drawable.lingo_writing
            else -> R.drawable.lingo
        }
        return this.copy(headerImageRes = newImage)
    }
}