package com.iti.linguaquest.features.auth.presentation.login.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.auth.presentation.login.contract.LoginEffect
import com.iti.linguaquest.features.auth.presentation.login.contract.LoginIntent
import com.iti.linguaquest.features.auth.presentation.login.viewmodel.LoginViewModel
import com.iti.linguaquest.features.auth.share.launchGoogleSignIn

@Composable
fun LoginScreen(
    onSignUp: () -> Unit,
    onSignUpWithoutLanguages: () -> Unit,
    onForgotPassword: () -> Unit,
    onLoginSuccess: () -> Unit,
    onOAuthLanguageSelection: () -> Unit,
    isOAuthLanguageSelectionCompleted: Boolean = false,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val state by viewModel.state.collectAsStateWithLifecycle()
    var emailShakeTrigger by remember { mutableIntStateOf(0) }
    var passwordShakeTrigger by remember { mutableIntStateOf(0) }
    var googleShakeTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(isOAuthLanguageSelectionCompleted) {
        if (isOAuthLanguageSelectionCompleted) {
            viewModel.onIntent(LoginIntent.OAuthLanguageSelectionCompleted)
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                LoginEffect.ShakeEmail -> emailShakeTrigger++
                LoginEffect.ShakePassword -> passwordShakeTrigger++
                LoginEffect.ShakeGoogleSignIn -> googleShakeTrigger++
                LoginEffect.LaunchGoogleSignIn -> {
                    activity?.launchGoogleSignIn(
                        onTokenReceived = { viewModel.onIntent(LoginIntent.GoogleLoginSubmitted(it)) },
                        onError = { viewModel.onIntent(LoginIntent.GoogleSignInFailed) }
                    )
                }
                LoginEffect.LoginSucceeded -> onLoginSuccess()
                LoginEffect.NavigateToForgotPassword -> onForgotPassword()
                LoginEffect.NavigateToSignUp -> onSignUp()
                LoginEffect.NavigateToSignUpWithoutLanguages -> onSignUpWithoutLanguages()
                LoginEffect.NavigateToOAuthLanguageSelection -> onOAuthLanguageSelection()
            }
        }
    }

    LoginContent(
        state = state,
        onIntent = viewModel::onIntent,
        emailShakeTrigger = emailShakeTrigger,
        passwordShakeTrigger = passwordShakeTrigger,
        googleShakeTrigger = googleShakeTrigger
    )
}
