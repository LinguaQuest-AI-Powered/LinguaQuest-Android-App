package com.iti.linguaquest.features.auth.presentation.otp.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.auth.presentation.login.view.LoginDimens
import com.iti.linguaquest.features.auth.presentation.otp.contract.OTPEffect
import com.iti.linguaquest.features.auth.presentation.otp.contract.OTPIntent
import com.iti.linguaquest.features.auth.presentation.otp.contract.OTPState
import com.iti.linguaquest.features.auth.presentation.otp.viewmodel.OTPViewModel
import com.iti.linguaquest.features.auth.presentation.ui.screen.OtpInputField
import com.iti.linguaquest.features.auth.share.components.AuthCardLayout

@Composable
fun OTPScreen(
    viewModel: OTPViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToNext: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is OTPEffect.NavigateBack -> onNavigateBack()
                is OTPEffect.NavigateToLogin -> onNavigateToLogin()
                is OTPEffect.NavigateToNextScreen -> onNavigateToNext()
                is OTPEffect.ShowError -> {  }
            }
        }
    }

    OTPContent(
        state = state,
        onIntent = { intent -> viewModel.onIntent(intent) }
    )
}

@Composable
fun OTPContent(
    state: OTPState,
    onIntent: (OTPIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        Box(
            modifier = Modifier
                .padding(top = 48.dp, start = 24.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary)
                .clickable { onIntent(OTPIntent.OnBackClicked) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = LoginDimens.ScreenPadding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = LoginDimens.ScreenPadding * 2),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(LoginDimens.ScreenPadding * 2))

            AuthCardLayout(
                imageRes = R.drawable.lingo_mail,
                titleRes = R.string.otp_verify_title,
                subtitleRes = R.string.otp_verify_subtitle
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                OtpInputField(
                    otpCode = state.otpCode,
                    onOtpChanged = { onIntent(OTPIntent.OnOtpCodeChanged(it)) },
                    onKeyboardDone = {
                        if (state.isVerifyEnabled && !state.isLoading) {
                            onIntent(OTPIntent.OnVerifyClicked)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_timer),
                        contentDescription = "Timer",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = state.timerText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                TextButton(
                    onClick = { onIntent(OTPIntent.OnResendCodeClicked) },
                    enabled = !state.isTimerActive && !state.isLoading,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.otp_resend_code),
                        style = MaterialTheme.typography.labelLarge,
                        color = if (!state.isTimerActive) AppColors.Teal else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.38f),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                AppButton(
                    text = stringResource(id = R.string.otp_verify_button),
                    onClick = { onIntent(OTPIntent.OnVerifyClicked) },
                    enabled = state.isVerifyEnabled && !state.isLoading,
                    isLoading = state.isLoading,
                    variant = ButtonVariant.PRIMARY,
                    icon = painterResource(id = R.drawable.verfiy),
                    iconPosition = IconPosition.END
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.clickable(enabled = !state.isLoading) {
                        onIntent(OTPIntent.OnBackToLoginClicked)
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = AppColors.Teal,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(id = R.string.otp_back_to_login),
                        style = MaterialTheme.typography.labelLarge,
                        color = AppColors.Teal,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(LoginDimens.ScreenPadding))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OTPContentPreview() {
    LinguaQuestTheme {
        OTPContent(
            state = OTPState(otpCode = "12"),
            onIntent = {}
        )
    }
}
