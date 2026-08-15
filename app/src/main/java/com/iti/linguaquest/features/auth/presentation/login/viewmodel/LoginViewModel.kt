package com.iti.linguaquest.features.auth.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.utils.ValidationUtils
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.usecase.LoginUserUseCase
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.auth.domain.usecase.SignInWithGoogleUseCase
import com.iti.linguaquest.features.auth.presentation.login.contract.LoginEffect
import com.iti.linguaquest.features.auth.presentation.login.contract.LoginIntent
import com.iti.linguaquest.features.auth.presentation.login.contract.LoginState
import com.iti.linguaquest.features.auth.presentation.login.mapper.toMessageRes
import com.iti.linguaquest.features.auth.domain.usecase.CompleteOAuthProfileUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetNativeLanguageUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetTargetLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val loginWithGoogleUseCase: SignInWithGoogleUseCase,
    private val snackbarController: SnackbarController,
    private val getTargetLanguageUseCase: GetTargetLanguageUseCase,
    private val getNativeLanguageUseCase: GetNativeLanguageUseCase,
    private val completeOAuthProfileUseCase: CompleteOAuthProfileUseCase,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effects = Channel<LoginEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

     val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.LoginClicked -> handleLoginClicked(intent.email, intent.password)

            LoginIntent.GoogleSignInClicked -> startGoogleSignIn()

            is LoginIntent.GoogleLoginSubmitted -> loginWithGoogle(intent.idToken)

            LoginIntent.GoogleSignInFailed -> handleGoogleFailure(AuthError.Unknown)

            LoginIntent.ForgetPasswordClicked -> sendEffect(LoginEffect.NavigateToForgotPassword)


            LoginIntent.SignUpClicked -> {
                viewModelScope.launch {
                    val targetLanguage = getTargetLanguageUseCase().first()
                    if (targetLanguage == null) {
                        sendEffect(LoginEffect.NavigateToSignUpWithoutLanguages)
                    } else {
                        sendEffect(LoginEffect.NavigateToSignUp)
                    }
                }
            }
            LoginIntent.OAuthLanguageSelectionCompleted -> {
                completeOAuthProfile()
            }

        }
    }

    private fun handleLoginClicked(email: String, password: String) {
        val emailValid = ValidationUtils.isValidEmail(email)
        val passwordValidationError = ValidationUtils.getPasswordValidationErrorRes(password)
        val passwordValid = passwordValidationError == null

        if (!emailValid) {
            _state.update { it.copy(
                emailError = true,
                emailErrorRes = if (email.isBlank()) R.string.login_error_email_required else R.string.login_error_invalid_email
            ) }
            sendEffect(LoginEffect.ShakeEmail)
        }
        if (!passwordValid) {
            _state.update { it.copy(
                passwordError = true,
                passwordErrorRes = passwordValidationError
            ) }
            sendEffect(LoginEffect.ShakePassword)
        }

        if (emailValid && passwordValid) {
            loginWithEmail(email, password)
        }
    }

    private fun loginWithEmail(email: String, password: String) {
        if (_state.value.isLoading) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalErrorRes = null) }
            when (val result = loginUserUseCase(email, password)) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    sendEffect(LoginEffect.LoginSucceeded)
                }

                is LinguaQuestResult.Failure -> handleAuthFailure(result.error, email)
            }
        }
    }

    private fun loginWithGoogle(idToken: String) {
        if (_state.value.isLoading) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalErrorRes = null) }
            when (val result = loginWithGoogleUseCase(idToken)) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isLoading = false, googleError = false) }
                    val profileComplete = result.data
                    if (profileComplete) {
                        sendEffect(LoginEffect.LoginSucceeded)
                    } else {
                        val targetLanguage = getTargetLanguageUseCase().first()
                        if (targetLanguage != null) {
                            completeOAuthProfile()
                        } else {
                            sendEffect(LoginEffect.NavigateToOAuthLanguageSelection)
                        }
                    }
                }

                is LinguaQuestResult.Failure -> handleGoogleFailure(result.error)
            }
        }
    }

    private fun completeOAuthProfile() {
        if (_state.value.isLoading) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalErrorRes = null) }
            val targetLanguage = getTargetLanguageUseCase().first() ?: 1
            val nativeLanguage = getNativeLanguageUseCase().first() ?: 1

            when (val result = completeOAuthProfileUseCase(nativeLanguage, targetLanguage, null)) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    sendEffect(LoginEffect.LoginSucceeded)
                }
                is LinguaQuestResult.Failure -> {
                    handleAuthFailure(result.error)
                }
            }
        }
    }

    private fun handleAuthFailure(error: AuthError, email: String? = null) {
        val emailHasError = error == AuthError.InvalidCredentials || error == AuthError.InvalidEmail || error == AuthError.EmailNotFound
        val passwordHasError = error == AuthError.InvalidCredentials || error == AuthError.WeakPassword || error == AuthError.InvalidPassword
        val isEmailNotVerified = error == AuthError.EmailNotVerified

        _state.update {
            it.copy(
                isLoading = false,
                generalErrorRes = if (!emailHasError && !passwordHasError && !isEmailNotVerified) error.toMessageRes() else null,
                emailError = emailHasError,
                emailErrorRes = if (emailHasError) error.toMessageRes() else null,
                passwordError = passwordHasError,
                passwordErrorRes = if (passwordHasError) error.toMessageRes() else null,
                googleError = false
            )
        }

        if (error == AuthError.InvalidCredentials || (!emailHasError && !passwordHasError && !isEmailNotVerified)) {
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
            AuthError.InvalidEmail -> sendEffect(LoginEffect.ShakeEmail)
            AuthError.WeakPassword -> sendEffect(LoginEffect.ShakePassword)
            AuthError.InvalidCredentials -> {
                sendEffect(LoginEffect.ShakeEmail)
                sendEffect(LoginEffect.ShakePassword)
            }
            AuthError.EmailNotVerified -> {
                email?.let { sendEffect(LoginEffect.NavigateToOTP(it)) }
            }

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
        sendEffect(LoginEffect.ShakeGoogleSignIn)
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
        sendEffect(LoginEffect.LaunchGoogleSignIn)
    }

    private fun sendEffect(effect: LoginEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}