package com.iti.linguaquest.features.auth.presentation.signup.view

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.core.utils.ValidationUtils
import com.iti.linguaquest.features.auth.presentation.login.view.LoginDimens
import com.iti.linguaquest.features.auth.presentation.signup.contract.SignUpIntent
import com.iti.linguaquest.features.auth.presentation.signup.contract.SignUpState
import com.iti.linguaquest.features.auth.share.components.AuthCardLayout
import com.iti.linguaquest.features.auth.share.components.AuthDivider
import com.iti.linguaquest.features.auth.share.components.AuthFooter
import com.iti.linguaquest.features.auth.share.components.AuthTextField
import com.iti.linguaquest.features.auth.share.components.shake

@Composable
fun SignUpContent(
    state: SignUpState,
    onIntent: (SignUpIntent) -> Unit,
    usernameShakeTrigger: Int,
    emailShakeTrigger: Int,
    passwordShakeTrigger: Int,
    confirmPasswordShakeTrigger: Int,
    googleShakeTrigger: Int,
    modifier: Modifier = Modifier,
) {
    var localUsernameShakeTrigger by remember { mutableIntStateOf(0) }
    var localEmailShakeTrigger by remember { mutableIntStateOf(0) }
    var localPasswordShakeTrigger by remember { mutableIntStateOf(0) }
    var localConfirmPasswordShakeTrigger by remember { mutableIntStateOf(0) }

    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

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
                titleRes = R.string.signup_title,
                subtitleRes = R.string.signup_subtitle
            ) {

                AuthTextField(
                    value = state.username,
                    onValueChange = { onIntent(SignUpIntent.UsernameChanged(it)) },
                    placeholder = stringResource(id = R.string.signup_username),
                    leadingIcon = rememberVectorPainter(image = Icons.Default.Person),
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { emailFocusRequester.requestFocus() }),
                    isError = state.usernameError,
                    errorMessage = state.usernameErrorRes?.let { stringResource(id = it) },
                    modifier = Modifier.shake(usernameShakeTrigger)
                )

                AuthTextField(
                    value = state.email,
                    onValueChange = { onIntent(SignUpIntent.EmailChanged(it)) },
                    placeholder = stringResource(id = R.string.login_email_address),
                    leadingIcon = painterResource(id = R.drawable.email),
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),
                    isError = state.emailError,
                    errorMessage = state.emailErrorRes?.let { stringResource(id = it) },
                    modifier = Modifier
                        .shake(emailShakeTrigger)
                        .focusRequester(emailFocusRequester)
                )

                AuthTextField(
                    value = state.password,
                    onValueChange = { onIntent(SignUpIntent.PasswordChanged(it)) },
                    placeholder = stringResource(id = R.string.login_password),
                    leadingIcon = painterResource(id = R.drawable.lock),
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { confirmPasswordFocusRequester.requestFocus() }),
                    isError = state.passwordError,
                    errorMessage = state.passwordErrorRes?.let { stringResource(id = it) },
                    enabled = true,
                    isPassword = true,
                    modifier = Modifier
                        .shake(passwordShakeTrigger)
                        .focusRequester(passwordFocusRequester)
                )

                AuthTextField(
                    value = state.confirmPassword,
                    onValueChange = { onIntent(SignUpIntent.ConfirmPasswordChanged(it)) },
                    placeholder = stringResource(id = R.string.signup_confirm_password),
                    leadingIcon = painterResource(id = R.drawable.lock),
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        onIntent(SignUpIntent.SignUpClicked)
                    }),
                    isError = state.confirmPasswordError,
                    errorMessage = state.confirmPasswordErrorRes?.let { stringResource(id = it) },
                    enabled = true,
                    isPassword = true,
                    modifier = Modifier
                        .shake(confirmPasswordShakeTrigger)
                        .focusRequester(confirmPasswordFocusRequester)
                )

                if (state.generalErrorRes != null) {
                    Text(
                        text = stringResource(id = state.generalErrorRes),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                AppButton(
                    text = stringResource(id = R.string.signup_button),
                    onClick = {
                        focusManager.clearFocus()
                        onIntent(SignUpIntent.SignUpClicked)
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
                    onClick = { onIntent(SignUpIntent.GoogleSignInClicked) },
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
                    textRes = R.string.signup_already_have_account,
                    actionRes = R.string.login_log_in,
                    onActionClick = { onIntent(SignUpIntent.LoginClicked) }
                )
            }
            Spacer(modifier = Modifier.height(LoginDimens.ScreenPadding))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpContentPreview() {
    LinguaQuestTheme {
        SignUpContent(
            state = SignUpState(),
            onIntent = {},
            usernameShakeTrigger = 0,
            emailShakeTrigger = 0,
            passwordShakeTrigger = 0,
            confirmPasswordShakeTrigger = 0,
            googleShakeTrigger = 0
        )
    }
}
