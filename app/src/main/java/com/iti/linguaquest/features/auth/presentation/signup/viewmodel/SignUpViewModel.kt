package com.iti.linguaquest.features.auth.presentation.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.core.utils.ValidationUtils
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.usecase.RegisterUserUseCase
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
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    private val _effects = Channel<SignUpEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.UsernameChanged -> {
                _state.update { it.copy(username = intent.username, usernameError = false, usernameErrorRes = null).withUpdatedImage() }
            }
            is SignUpIntent.EmailChanged -> {
                _state.update { it.copy(email = intent.email, emailError = false, emailErrorRes = null).withUpdatedImage() }
            }
            is SignUpIntent.PasswordChanged -> {
                _state.update { it.copy(password = intent.password, passwordError = false, passwordErrorRes = null).withUpdatedImage() }
            }
            is SignUpIntent.ConfirmPasswordChanged -> {
                _state.update { it.copy(confirmPassword = intent.password, confirmPasswordError = false, confirmPasswordErrorRes = null).withUpdatedImage() }
            }
            SignUpIntent.SignUpClicked -> handleSignUpClicked()
            SignUpIntent.LoginClicked -> sendEffect(SignUpEffect.NavigateToLogin)
            SignUpIntent.GoogleSignInClicked -> startGoogleSignIn()
            is SignUpIntent.GoogleLoginSubmitted -> loginWithGoogle(intent.idToken)
            SignUpIntent.GoogleSignInFailed -> handleGoogleFailure(AuthError.Unknown)
        }
    }

    private fun handleSignUpClicked() {
        val username = state.value.username
        val email = state.value.email
        val password = state.value.password
        val confirmPassword = state.value.confirmPassword

        val usernameValid = ValidationUtils.isValidName(username)
        val emailValid = ValidationUtils.isValidEmail(email)
        val passwordValid = ValidationUtils.isValidPassword(password)
        val passwordsMatch = password == confirmPassword && password.isNotEmpty()

        if (!usernameValid) {
            _state.update { it.copy(
                usernameError = true,
                usernameErrorRes = if (username.isBlank()) R.string.signup_error_name_required else R.string.signup_error_name_too_short
            ).withUpdatedImage() }
            sendEffect(SignUpEffect.ShakeUsername)
        }
        if (!emailValid) {
            _state.update { it.copy(
                emailError = true,
                emailErrorRes = if (email.isBlank()) R.string.login_error_email_required else R.string.login_error_invalid_email
            ).withUpdatedImage() }
            sendEffect(SignUpEffect.ShakeEmail)
        }
        if (!passwordValid) {
            _state.update { it.copy(
                passwordError = true,
                passwordErrorRes = if (password.isBlank()) R.string.login_error_password_required else R.string.login_error_weak_password
            ).withUpdatedImage() }
            sendEffect(SignUpEffect.ShakePassword)
        }
        if (!passwordsMatch && passwordValid) {
            _state.update { it.copy(
                confirmPasswordError = true,
                confirmPasswordErrorRes = R.string.signup_error_passwords_do_not_match
            ).withUpdatedImage() }
            sendEffect(SignUpEffect.ShakeConfirmPassword)
        }

        if (usernameValid && emailValid && passwordValid && passwordsMatch) {
            signUpWithEmail(username, email, password)
        }
    }

    private fun signUpWithEmail(username: String, email: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalErrorRes = null) }
//            when (val result = signUpWithEmailUseCase(username, email, password)) {
//                is LinguaQuestResult.Success -> {
//                    _state.update { it.copy(isLoading = false) }
//                    sendEffect(SignUpEffect.SignUpSucceeded)
//                }
//                is LinguaQuestResult.Failure -> handleAuthFailure(result.error)
//            }
        }
    }

    private fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalErrorRes = null) }
            when (val result = loginWithGoogleUseCase(idToken)) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isLoading = false, googleError = false) }
                    sendEffect(SignUpEffect.SignUpSucceeded)
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
                generalErrorRes = error.toMessageRes(),
            ).withUpdatedImage()
        }
        sendEffect(SignUpEffect.ShakeGoogleSignIn)
    }

    private fun startGoogleSignIn() {
        _state.update {
            it.copy(
                googleError = false,
                generalErrorRes = null,
            ).withUpdatedImage()
        }
        sendEffect(SignUpEffect.LaunchGoogleSignIn)
    }

    private fun sendEffect(effect: SignUpEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }

    private fun SignUpState.withUpdatedImage(): SignUpState {
        val newImage = when {
            usernameError || emailError || passwordError || confirmPasswordError || generalErrorRes != null -> R.drawable.lingo_error
            username.isNotBlank() || email.isNotBlank() || password.isNotBlank() || confirmPassword.isNotBlank() -> R.drawable.lingo_writing
            else -> R.drawable.lingo_register
        }
        return this.copy(headerImageRes = newImage)
    }
}
