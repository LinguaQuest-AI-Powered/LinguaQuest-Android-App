package com.iti.linguaquest.features.auth.presentation.newpassword.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.features.auth.presentation.newpassword.contract.NewPasswordEffect
import com.iti.linguaquest.features.auth.presentation.newpassword.contract.NewPasswordIntent
import com.iti.linguaquest.features.auth.presentation.newpassword.contract.NewPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.usecase.SetNewPasswordUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MIN_PASSWORD_LENGTH = 8

@HiltViewModel
class NewPasswordViewModel @Inject constructor(
    private val setNewPasswordUseCase: SetNewPasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NewPasswordState())
    val state = _state.asStateFlow()

    private val _effects = Channel<NewPasswordEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: NewPasswordIntent) {
        when (intent) {
            is NewPasswordIntent.ResetPasswordClicked -> validateAndReset(intent.resetToken, intent.newPassword, intent.confirmPassword)

            NewPasswordIntent.BackToLoginClicked -> sendEffect(NewPasswordEffect.NavigateBackToLogin)
        }
    }

    private fun validateAndReset(resetToken: String, newPassword: String, confirmPassword: String) {
        val newPasswordValidation = validateNewPassword(newPassword)
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

    private fun validateNewPassword(password: String): Int? {
        return when {
            password.isBlank() -> R.string.new_password_error_required
            password.length < MIN_PASSWORD_LENGTH -> R.string.new_password_error_too_short
            !password.any { it.isUpperCase() } -> R.string.new_password_error_no_uppercase
            !password.any { it.isDigit() } -> R.string.new_password_error_no_number
            else -> null
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
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalErrorRes = null) }
            val result = setNewPasswordUseCase(newPassword, resetToken)
            _state.update { it.copy(isLoading = false) }
            if (result is LinguaQuestResult.Success) {
                sendEffect(NewPasswordEffect.ResetSucceeded)
            } else {
                _state.update { it.copy(generalErrorRes = R.string.new_password_error_generic) }
            }
        }
    }

    private fun sendEffect(effect: NewPasswordEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}
