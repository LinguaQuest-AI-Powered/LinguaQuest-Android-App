package com.iti.linguaquest.features.auth.presentation.login.view

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import com.iti.linguaquest.core.utils.ValidationUtils
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.features.auth.presentation.login.contract.LoginIntent
import com.iti.linguaquest.features.auth.presentation.login.contract.LoginState

import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.features.auth.share.components.AuthCardLayout
import com.iti.linguaquest.features.auth.share.components.AuthDivider
import com.iti.linguaquest.features.auth.share.components.AuthFooter
import com.iti.linguaquest.features.auth.share.components.AuthTextField
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.features.auth.share.components.shake

@Composable
fun LoginContent(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    emailShakeTrigger: Int,
    passwordShakeTrigger: Int,
    googleShakeTrigger: Int,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = remember { FocusRequester() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
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
                imageRes = state.headerImageRes,
                titleRes = R.string.login_welcome_back,
                subtitleRes = R.string.login_ready_to_continue
            ) {
                AuthTextField(
                    value = state.email,
                    onValueChange = { onIntent(LoginIntent.EmailChanged(it)) },
                    placeholder = stringResource(id = R.string.login_email_address),
                    leadingIcon = painterResource(id = R.drawable.email),
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),
                    isError = state.emailError,
                    errorMessage = state.emailErrorRes?.let { stringResource(id = it) },
                    modifier = Modifier.shake(emailShakeTrigger)
                )

                AuthTextField(
                    value = state.password,
                    onValueChange = { onIntent(LoginIntent.PasswordChanged(it)) },
                    placeholder = stringResource(id = R.string.login_password),
                    leadingIcon = painterResource(id = R.drawable.lock),
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        onIntent(LoginIntent.LoginClicked)
                    }),
                    isError = state.passwordError,
                    errorMessage = state.passwordErrorRes?.let { stringResource(id = it) },
                    enabled = true,
                    isPassword = true,
                    modifier = Modifier
                        .shake(passwordShakeTrigger)
                        .focusRequester(passwordFocusRequester)
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
                    onClick = {
                        focusManager.clearFocus()
                        onIntent(LoginIntent.LoginClicked)
                    },
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

private fun resolveHeroImageRes(email: String, password: String, localEmailError: Boolean, localPasswordError: Boolean, state: LoginState): Int {
    return when {
        hasErrorState(localEmailError, localPasswordError, state) -> R.drawable.lingo_error
        hasUserInput(email, password) -> R.drawable.lingo_writing
        else -> R.drawable.lingo
    }
}

private fun hasUserInput(email: String, password: String): Boolean {
    return email.isNotBlank() || password.isNotBlank()
}

private fun hasErrorState(localEmailError: Boolean, localPasswordError: Boolean, state: LoginState): Boolean {
    return localEmailError || localPasswordError || state.emailError || state.passwordError || state.generalErrorRes != null
}