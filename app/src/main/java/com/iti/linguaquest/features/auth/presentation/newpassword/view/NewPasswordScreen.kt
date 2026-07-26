package com.iti.linguaquest.features.auth.presentation.newpassword.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.features.auth.presentation.newpassword.contract.NewPasswordEffect
import com.iti.linguaquest.features.auth.presentation.newpassword.contract.NewPasswordIntent
import com.iti.linguaquest.features.auth.presentation.newpassword.viewmodel.NewPasswordViewModel

@Composable
fun NewPasswordScreen(
    resetToken: String,
    onBackToLogin: () -> Unit,
    onResetSuccess: () -> Unit,
    viewModel: NewPasswordViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var newPasswordShakeTrigger by remember { mutableIntStateOf(0) }
    var confirmPasswordShakeTrigger by remember { mutableIntStateOf(0) }
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                NewPasswordEffect.ShakeNewPassword -> newPasswordShakeTrigger++
                NewPasswordEffect.ShakeConfirmPassword -> confirmPasswordShakeTrigger++
                NewPasswordEffect.ResetSucceeded -> onResetSuccess()
                NewPasswordEffect.NavigateBackToLogin -> onBackToLogin()
            }
        }
    }
    OfflineAwareContent(isOnline = isOnline) {
        NewPasswordContent(
            state = state,
            onIntent = { intent ->
                when (intent) {
                    is NewPasswordIntent.ResetPasswordClicked -> {
                        viewModel.onIntent(
                            NewPasswordIntent.ResetPasswordClicked(
                                resetToken = resetToken,
                                newPassword = intent.newPassword,
                                confirmPassword = intent.confirmPassword
                            )
                        )
                    }

                    else -> viewModel.onIntent(intent)
                }
            },
            newPasswordShakeTrigger = newPasswordShakeTrigger,
            confirmPasswordShakeTrigger = confirmPasswordShakeTrigger,
        )
    }
}
