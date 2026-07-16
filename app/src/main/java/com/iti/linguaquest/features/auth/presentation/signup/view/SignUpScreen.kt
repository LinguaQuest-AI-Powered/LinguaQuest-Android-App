
package com.iti.linguaquest.features.auth.presentation.signup.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.auth.presentation.signup.contract.SignUpEffect
import com.iti.linguaquest.features.auth.presentation.signup.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    onNavigateToLogin: () -> Unit,
    onSignUpSuccess: (String) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
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
