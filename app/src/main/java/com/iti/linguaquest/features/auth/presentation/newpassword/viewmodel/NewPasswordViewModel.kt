package com.iti.linguaquest.features.auth.presentation.newpassword.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.features.auth.presentation.newpassword.contract.NewPasswordEffect
import com.iti.linguaquest.features.auth.presentation.newpassword.contract.NewPasswordIntent
import com.iti.linguaquest.features.auth.presentation.newpassword.contract.NewPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.utils.ValidationUtils
import com.iti.linguaquest.features.auth.domain.usecase.SetNewPasswordUseCase
import com.iti.linguaquest.features.auth.presentation.login.mapper.toMessageRes
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class NewPasswordViewModel @Inject constructor(
    private val setNewPasswordUseCase: SetNewPasswordUseCase,
    private val snackbarController: SnackbarController,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(NewPasswordState())
    val state = _state.asStateFlow()

    private val _effects = Channel<NewPasswordEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()
    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )
    fun onIntent(intent: NewPasswordIntent) {
        when (intent) {
            is NewPasswordIntent.ResetPasswordClicked -> validateAndReset(intent.resetToken, intent.newPassword, intent.confirmPassword)

            NewPasswordIntent.BackToLoginClicked -> sendEffect(NewPasswordEffect.NavigateBackToLogin)
        }
    }

    private fun validateAndReset(resetToken: String, newPassword: String, confirmPassword: String) {
        val newPasswordValidation = ValidationUtils.getPasswordValidationErrorRes(newPassword, R.string.new_password_error_required)
        val confirmPasswordValidation = validateConfirmPassword(newPassword, confirmPassword)

        val hasNewPasswordError = newPasswordValidation != null
        val hasConfirmPasswordError = confirmPasswordValidation != null

        _state.update {
            it.copy(
                newPasswordError = hasNewPasswordError,
                newPasswordErrorRes = newPasswordValidation,
                confirmPasswordError = hasConfirmPasswordError,
                confirmPasswordErrorRes = confirmPasswordValidation,
                generalErrorRes = null,
            )
        }

        if (hasNewPasswordError) sendEffect(NewPasswordEffect.ShakeNewPassword)
        if (hasConfirmPasswordError) sendEffect(NewPasswordEffect.ShakeConfirmPassword)

        if (!hasNewPasswordError && !hasConfirmPasswordError) {
            performReset(resetToken, newPassword)
        }
    }

    private fun validateConfirmPassword(newPassword: String, confirmPassword: String): Int? {
        return when {
            confirmPassword.isBlank() -> R.string.new_password_error_confirm_required
            newPassword != confirmPassword -> R.string.new_password_error_no_match
            else -> null
        }
    }

    private fun performReset(resetToken: String, newPassword: String) {
        if (_state.value.isLoading) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalErrorRes = null) }
            val result = setNewPasswordUseCase(newPassword, resetToken)
            _state.update { it.copy(isLoading = false) }
            when (result) {
                is LinguaQuestResult.Success -> {
                    sendEffect(NewPasswordEffect.ResetSucceeded)
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

    private fun sendEffect(effect: NewPasswordEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}
