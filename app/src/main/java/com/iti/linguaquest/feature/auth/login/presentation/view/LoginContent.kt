package com.iti.linguaquest.features.auth.login.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.features.auth.login.presentation.contract.LoginIntent
import com.iti.linguaquest.features.auth.login.presentation.contract.LoginState
import com.iti.linguaquest.feature.auth.share.components.AppButton
import com.iti.linguaquest.feature.auth.share.components.AuthCardLayout
import com.iti.linguaquest.feature.auth.share.components.AuthDivider
import com.iti.linguaquest.feature.auth.share.components.AuthFooter
import com.iti.linguaquest.feature.auth.share.components.AuthTextField
import com.iti.linguaquest.feature.auth.share.components.ButtonVariant
import com.iti.linguaquest.feature.auth.share.components.IconPosition
import com.iti.linguaquest.feature.auth.share.components.shake
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun LoginContent(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    emailShakeTrigger: Int,
    passwordShakeTrigger: Int,
    googleShakeTrigger: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFF3FAFF)) // Soft light blue from design
            .padding(horizontal = LoginDimens.ScreenPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = LoginDimens.ScreenPadding * 2),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(LoginDimens.ScreenPadding * 2))
            
            AuthCardLayout(
                imageRes = state.resolveHeroImageRes(),
                titleRes = R.string.login_welcome_back,
                subtitleRes = R.string.login_ready_to_continue
            ) {
                AuthTextField(
                    value = state.email,
                    onValueChange = { onIntent(LoginIntent.EmailChanged(it)) },
                    placeholder = stringResource(id = R.string.login_email_address),
                    leadingIcon = painterResource(id = R.drawable.email),
                    isError = state.emailError,
                    errorMessage = state.emailErrorRes?.let { stringResource(id = it) },
                    modifier = Modifier.shake(emailShakeTrigger)
                )

                AuthTextField(
                    value = state.password,
                    onValueChange = { onIntent(LoginIntent.PasswordChanged(it)) },
                    placeholder = stringResource(id = R.string.login_password),
                    leadingIcon = painterResource(id = R.drawable.lock),
                    isError = state.passwordError,
                    errorMessage = state.passwordErrorRes?.let { stringResource(id = it) },
                    enabled = true,
                    isPassword = true,
                    modifier = Modifier.shake(passwordShakeTrigger)
                )

                TextButton(
                    onClick = { onIntent(LoginIntent.ForgetPasswordClicked) },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text(
                        text = stringResource(id = R.string.login_forgot_password),
                        style = MaterialTheme.typography.labelLarge,
                        color = AppColors.Teal,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (state.generalErrorRes != null) {
                    Text(
                        text = stringResource(id = state.generalErrorRes),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                AppButton(
                    text = stringResource(id = R.string.login_log_in),
                    onClick = { onIntent(LoginIntent.LoginClicked) },
                    enabled = !state.isLoading,
                    isLoading = state.isLoading,
                    variant = ButtonVariant.PRIMARY,
                    icon = painterResource(id = R.drawable.arrow_right),
                    iconPosition = IconPosition.END
                )

                AuthDivider(textRes = R.string.login_or_continue_with)

                AppButton(
                    text = stringResource(id = R.string.login_google),
                    onClick = { onIntent(LoginIntent.GoogleSignInClicked) },
                    enabled = !state.isLoading,
                    isError = state.googleError,
                    variant = ButtonVariant.SOCIAL,
                    icon = painterResource(id = R.drawable.google),
                    iconPosition = IconPosition.START,
                    tintIcon = false,
                    modifier = Modifier.shake(googleShakeTrigger)
                )

                Spacer(modifier = Modifier.height(8.dp))

                AuthFooter(
                    textRes = R.string.login_new_here,
                    actionRes = R.string.login_sign_up,
                    onActionClick = { onIntent(LoginIntent.SignUpClicked) }
                )
            }
            Spacer(modifier = Modifier.height(LoginDimens.ScreenPadding))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginContentPreview() {
    LinguaQuestTheme {
        LoginContent(
            state = LoginState(),
            onIntent = {},
            emailShakeTrigger = 0,
            passwordShakeTrigger = 0,
            googleShakeTrigger = 0
        )
    }
}

private fun LoginState.resolveHeroImageRes(): Int {
    return when {
        hasErrorState() -> R.drawable.loginerror
        hasUserInput() -> R.drawable.loginwriting
        else -> R.drawable.logindefault
    }
}

private fun LoginState.hasUserInput(): Boolean {
    return email.isNotBlank() || password.isNotBlank()
}

private fun LoginState.hasErrorState(): Boolean {
    return emailError || passwordError || generalErrorRes != null
}