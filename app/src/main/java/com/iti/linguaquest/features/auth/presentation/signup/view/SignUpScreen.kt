
package com.iti.linguaquest.features.auth.presentation.signup.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.auth.presentation.signup.contract.SignUpEffect
import com.iti.linguaquest.features.auth.presentation.signup.contract.SignUpIntent
import com.iti.linguaquest.features.auth.presentation.signup.viewmodel.SignUpViewModel
import com.iti.linguaquest.features.auth.share.launchGoogleSignIn

@Composable
fun SignUpScreen(
    onNavigateToLogin: () -> Unit,
    onSignUpSuccess: (String) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val state by viewModel.state.collectAsStateWithLifecycle()

    var usernameShakeTrigger by remember { mutableIntStateOf(0) }
    var emailShakeTrigger by remember { mutableIntStateOf(0) }
    var passwordShakeTrigger by remember { mutableIntStateOf(0) }
    var confirmPasswordShakeTrigger by remember { mutableIntStateOf(0) }
    var googleShakeTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is SignUpEffect.SignUpSucceeded -> onSignUpSuccess(effect.email)
                is SignUpEffect.NavigateToLogin -> onNavigateToLogin()
                is SignUpEffect.LaunchGoogleSignIn -> {
                    activity?.launchGoogleSignIn(
                        onTokenReceived = { viewModel.onIntent(SignUpIntent.GoogleLoginSubmitted(it)) },
                        onError = { viewModel.onIntent(SignUpIntent.GoogleSignInFailed) }
                    )
                }
                SignUpEffect.ShakeUsername -> usernameShakeTrigger++
                SignUpEffect.ShakeEmail -> emailShakeTrigger++
                SignUpEffect.ShakePassword -> passwordShakeTrigger++
                SignUpEffect.ShakeConfirmPassword -> confirmPasswordShakeTrigger++
                SignUpEffect.ShakeGoogleSignIn -> googleShakeTrigger++
            }
        }
    }

    SignUpContent(
        state = state,
        onIntent = viewModel::onIntent,
        usernameShakeTrigger = usernameShakeTrigger,
        emailShakeTrigger = emailShakeTrigger,
        passwordShakeTrigger = passwordShakeTrigger,
        confirmPasswordShakeTrigger = confirmPasswordShakeTrigger,
        googleShakeTrigger = googleShakeTrigger
    )
}
