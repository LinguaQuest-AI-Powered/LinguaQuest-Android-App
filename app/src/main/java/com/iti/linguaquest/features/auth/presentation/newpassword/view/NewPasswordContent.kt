package com.iti.linguaquest.features.auth.presentation.newpassword.view

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.auth.presentation.newpassword.contract.NewPasswordIntent
import com.iti.linguaquest.features.auth.presentation.newpassword.contract.NewPasswordState
import com.iti.linguaquest.features.auth.presentation.newpassword.view.component.PasswordStrengthIndicator
import com.iti.linguaquest.features.auth.share.components.AuthCardLayout
import com.iti.linguaquest.features.auth.share.components.AuthTextField
import com.iti.linguaquest.features.auth.share.components.shake

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding

@Composable
fun NewPasswordContent(
    state: NewPasswordState,
    onIntent: (NewPasswordIntent) -> Unit,
    newPasswordShakeTrigger: Int,
    confirmPasswordShakeTrigger: Int,
    modifier: Modifier = Modifier,
) {
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var localNewPasswordError by rememberSaveable { mutableStateOf(false) }
    var localConfirmPasswordError by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    val onResetClick = {
        focusManager.clearFocus()
        onIntent(
            NewPasswordIntent.ResetPasswordClicked(
                newPassword = newPassword,
                confirmPassword = confirmPassword,
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = NewPasswordDimens.ScreenPadding, vertical = NewPasswordDimens.ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            AuthCardLayout(
                imageRes = resolveHeroImageRes(
                    newPassword = newPassword,
                    confirmPassword = confirmPassword,
                    localNewPasswordError = localNewPasswordError,
                    localConfirmPasswordError = localConfirmPasswordError,
                    state = state,
                ),
                titleRes = R.string.new_password_title,
                subtitleRes = R.string.new_password_subtitle,
            ) {
                AuthTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it; localNewPasswordError = false },
                    placeholder = stringResource(id = R.string.new_password_hint),
                    leadingIcon = painterResource(id = R.drawable.new_password_key),
                    isPassword = true,
                    disableCopyPaste = true,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { confirmPasswordFocusRequester.requestFocus() }),
                    isError = localNewPasswordError || state.newPasswordError,
                    errorMessage = state.newPasswordErrorRes?.let { stringResource(id = it) },
                    modifier = Modifier.shake(newPasswordShakeTrigger),
                )

                PasswordStrengthIndicator(password = newPassword)

                AuthTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; localConfirmPasswordError = false },
                    placeholder = stringResource(id = R.string.confirm_password_hint),
                    leadingIcon = painterResource(id = R.drawable.lock),
                    isPassword = true,
                    disableCopyPaste = true,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = { onResetClick() }),
                    isError = localConfirmPasswordError || state.confirmPasswordError,
                    errorMessage = state.confirmPasswordErrorRes?.let { stringResource(id = it) },
                    modifier = Modifier
                        .shake(confirmPasswordShakeTrigger)
                        .focusRequester(confirmPasswordFocusRequester),
                )

                if (state.generalErrorRes != null) {
                    Text(
                        text = stringResource(id = state.generalErrorRes),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                AppButton3D(
                    text = stringResource(id = R.string.new_password_reset_button),
                    onClick = onResetClick,
                    enabled = !state.isLoading,
                    isLoading = state.isLoading,
                    variant = ButtonVariant.PRIMARY,
                    icon = painterResource(id = R.drawable.arrow_right),
                    iconPosition = IconPosition.END,
                )

                TextButton(
                    onClick = { onIntent(NewPasswordIntent.BackToLoginClicked) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.new_password_back_to_login),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(NewPasswordDimens.ScreenPadding))
        }
    }
}

private fun resolveHeroImageRes(
    newPassword: String,
    confirmPassword: String,
    localNewPasswordError: Boolean,
    localConfirmPasswordError: Boolean,
    state: NewPasswordState,
): Int {
    val hasError = localNewPasswordError || localConfirmPasswordError
        || state.newPasswordError || state.confirmPasswordError
        || state.generalErrorRes != null
    val hasInput = newPassword.isNotBlank() || confirmPassword.isNotBlank()
    return when {
        hasError -> R.drawable.lingo_error
        hasInput -> R.drawable.lingo_writing
        else -> R.drawable.lingo_new_password
    }
}

@Preview(showBackground = true)
@Composable
private fun NewPasswordContentPreview() {
    LinguaQuestTheme {
        NewPasswordContent(
            state = NewPasswordState(),
            onIntent = {},
            newPasswordShakeTrigger = 0,
            confirmPasswordShakeTrigger = 0,
        )
    }
}
