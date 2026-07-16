package com.iti.linguaquest.features.auth.presentation.forgetpassword.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.auth.presentation.forgetpassword.contract.ForgetPasswordEffect
import com.iti.linguaquest.features.auth.presentation.forgetpassword.viewmodel.ForgetPasswordViewModel

@Composable
fun ForgetPasswordScreen(
    onBackToLogin: () -> Unit,
    onSendSucceeded: (String) -> Unit,
    viewModel: ForgetPasswordViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var emailShakeTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                ForgetPasswordEffect.ShakeEmail -> emailShakeTrigger++
                is ForgetPasswordEffect.SendSucceeded -> onSendSucceeded(effect.email)
                ForgetPasswordEffect.NavigateBackToLogin -> onBackToLogin()
            }
        }
    }

    ForgetPasswordContent(
        state = state,
        onIntent = viewModel::onIntent,
        emailShakeTrigger = emailShakeTrigger,
    )
}
