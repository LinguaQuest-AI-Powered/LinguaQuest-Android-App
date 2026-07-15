package com.iti.linguaquest.features.auth.presentation.forgetpassword.view

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.auth.presentation.forgetpassword.contract.ForgetPasswordIntent
import com.iti.linguaquest.features.auth.presentation.forgetpassword.contract.ForgetPasswordState
import com.iti.linguaquest.features.auth.share.components.AuthCardLayout
import com.iti.linguaquest.features.auth.share.components.AuthTextField
import com.iti.linguaquest.features.auth.share.components.shake

@Composable
fun ForgetPasswordContent(
    state: ForgetPasswordState,
    onIntent: (ForgetPasswordIntent) -> Unit,
    emailShakeTrigger: Int,
    modifier: Modifier = Modifier,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var localEmailError by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val onSendClick = {
        focusManager.clearFocus()
        onIntent(ForgetPasswordIntent.SendClicked(email = email))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = ForgetPasswordDimens.ScreenPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = ForgetPasswordDimens.BottomSpacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(ForgetPasswordDimens.ScreenPadding * 2))

            AuthCardLayout(
                imageRes = resolveHeroImageRes(
                    email = email,
                    localEmailError = localEmailError,
                    state = state,
                ),
                titleRes = R.string.forget_password_title,
                subtitleRes = R.string.forget_password_subtitle,
            ) {
                AuthTextField(
                    value = email,
                    onValueChange = { email = it; localEmailError = false },
                    placeholder = stringResource(id = R.string.forget_password_email_hint),
                    leadingIcon = painterResource(id = R.drawable.email),
                    isPassword = false,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = { onSendClick() }),
                    isError = localEmailError || state.emailError,
                    errorMessage = state.emailErrorRes?.let { stringResource(id = it) },
                    modifier = Modifier.shake(emailShakeTrigger),
                )

                if (state.generalErrorRes != null) {
                    Text(
                        text = stringResource(id = state.generalErrorRes),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                AppButton(
                    text = stringResource(id = R.string.forget_password_send_button),
                    onClick = onSendClick,
                    enabled = !state.isLoading,
                    isLoading = state.isLoading,
                    variant = ButtonVariant.PRIMARY,
                    icon = painterResource(id = R.drawable.arrow_right),
                    iconPosition = IconPosition.END,
                )

                TextButton(
                    onClick = { onIntent(ForgetPasswordIntent.BackToLoginClicked) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.new_password_back_to_login),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.Teal,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(ForgetPasswordDimens.ScreenPadding))
        }
    }
}

private fun resolveHeroImageRes(
    email: String,
    localEmailError: Boolean,
    state: ForgetPasswordState,
): Int {
    val hasError = localEmailError || state.emailError || state.generalErrorRes != null
    val hasInput = email.isNotBlank()
    return when {
        hasError -> R.drawable.lingo_error
        hasInput -> R.drawable.lingo_writing
        else -> R.drawable.lingo_forget_password
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgetPasswordContentPreview() {
    LinguaQuestTheme {
        ForgetPasswordContent(
            state = ForgetPasswordState(),
            onIntent = {},
            emailShakeTrigger = 0,
        )
    }
}
