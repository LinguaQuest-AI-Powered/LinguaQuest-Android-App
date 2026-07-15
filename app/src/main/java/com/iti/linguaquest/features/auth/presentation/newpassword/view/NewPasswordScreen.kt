package com.iti.linguaquest.features.auth.presentation.newpassword.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.auth.presentation.newpassword.contract.NewPasswordEffect
import com.iti.linguaquest.features.auth.presentation.newpassword.viewmodel.NewPasswordViewModel

@Composable
fun NewPasswordScreen(
    onBackToLogin: () -> Unit,
    onResetSuccess: () -> Unit,
    viewModel: NewPasswordViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var newPasswordShakeTrigger by remember { mutableIntStateOf(0) }
    var confirmPasswordShakeTrigger by remember { mutableIntStateOf(0) }

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

    NewPasswordContent(
        state = state,
        onIntent = viewModel::onIntent,
        newPasswordShakeTrigger = newPasswordShakeTrigger,
        confirmPasswordShakeTrigger = confirmPasswordShakeTrigger,
    )
}
