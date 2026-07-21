package com.iti.linguaquest.features.auth.presentation.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.utils.ValidationUtils
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.usecase.RegisterUserUseCase
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.auth.domain.usecase.SignInWithGoogleUseCase
import com.iti.linguaquest.features.auth.presentation.login.mapper.toMessageRes
import com.iti.linguaquest.features.auth.presentation.signup.contract.SignUpEffect
import com.iti.linguaquest.features.auth.presentation.signup.contract.SignUpIntent
import com.iti.linguaquest.features.auth.presentation.signup.contract.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpWithEmailUseCase: RegisterUserUseCase,
    private val loginWithGoogleUseCase: SignInWithGoogleUseCase,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    private val _effects = Channel<SignUpEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.SignUpClicked -> handleSignUpClicked(
                intent.username,
                intent.email,
                intent.password,
                intent.confirmPassword
            )
            SignUpIntent.LoginClicked -> sendEffect(SignUpEffect.NavigateToLogin)
            SignUpIntent.GoogleSignInClicked -> startGoogleSignIn()
            is SignUpIntent.GoogleLoginSubmitted -> loginWithGoogle(intent.idToken)
            SignUpIntent.GoogleSignInFailed -> handleGoogleFailure(AuthError.Unknown)
        }
    }

    private fun handleSignUpClicked(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        val usernameValid = ValidationUtils.isValidName(username)
        val emailValid = ValidationUtils.isValidEmail(email)
        val passwordValidationError = ValidationUtils.getPasswordValidationErrorRes(password)
        val passwordValid = passwordValidationError == null
        val passwordsMatch = password == confirmPassword && password.isNotEmpty()

        if (!usernameValid) {
            _state.update { it.copy(
                usernameError = true, 
                usernameErrorRes = if (username.isBlank()) R.string.signup_error_name_required else R.string.signup_error_name_too_short
            ) }
            sendEffect(SignUpEffect.ShakeUsername)
        }
        if (!emailValid) {
            _state.update { it.copy(
                emailError = true, 
                emailErrorRes = if (email.isBlank()) R.string.login_error_email_required else R.string.login_error_invalid_email
            ) }
            sendEffect(SignUpEffect.ShakeEmail)
        }
        if (!passwordValid) {
            _state.update { it.copy(
                passwordError = true,
                passwordErrorRes = passwordValidationError
            ) }
            sendEffect(SignUpEffect.ShakePassword)
        }
        if (!passwordsMatch && passwordValid) {
            _state.update { it.copy(
                confirmPasswordError = true,
                confirmPasswordErrorRes = R.string.signup_error_passwords_do_not_match
            ) }
            sendEffect(SignUpEffect.ShakeConfirmPassword)
        }

        if (usernameValid && emailValid && passwordValid && passwordsMatch) {
            signUpWithEmail(username, email, password)
        }
    }

    private fun signUpWithEmail(username: String, email: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalErrorRes = null) }
            when (val result = signUpWithEmailUseCase(email, username, password)) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    sendEffect(SignUpEffect.SignUpSucceeded(email))
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
                    sendEffect(SignUpEffect.SignUpSucceeded(""))
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
                generalErrorRes = if (!emailHasError && !passwordHasError) error.toMessageRes() else null,
                emailError = emailHasError,
                emailErrorRes = if (emailHasError) error.toMessageRes() else null,
                passwordError = passwordHasError,
                passwordErrorRes = if (passwordHasError) error.toMessageRes() else null,
                googleError = false
            )
        }

        if (!emailHasError && !passwordHasError) {
            viewModelScope.launch {
                snackbarController.sendEvent(
                    SnackbarEvent(
                        message = UiText.StringResource(error.toMessageRes()),
                        type = SnackbarType.ERROR
                    )
                )
            }
        }

        when (error) {
            AuthError.InvalidEmail -> sendEffect(SignUpEffect.ShakeEmail)
            AuthError.WeakPassword -> sendEffect(SignUpEffect.ShakePassword)
            else -> Unit
        }
    }

    private fun handleGoogleFailure(error: AuthError) {
        _state.update {
            it.copy(
                isLoading = false,
                googleError = true,
                generalErrorRes = error.toMessageRes()
            )
        }
        sendEffect(SignUpEffect.ShakeGoogleSignIn)
        viewModelScope.launch {
            snackbarController.sendEvent(
                SnackbarEvent(
                    message = UiText.StringResource(error.toMessageRes()),
                    type = SnackbarType.ERROR
                )
            )
        }
    }

    private fun startGoogleSignIn() {
        _state.update {
            it.copy(
                googleError = false,
                generalErrorRes = null,
            )
        }
        sendEffect(SignUpEffect.LaunchGoogleSignIn)
    }

    private fun sendEffect(effect: SignUpEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}
