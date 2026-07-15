package com.iti.linguaquest.features.auth.presentation.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.features.auth.presentation.contract.OTPState
import com.iti.linguaquest.ui.theme.AppTextStyles
import com.iti.linguaquest.ui.theme.LinguaQuestTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.linguaquest.features.auth.presentation.contract.OTPEffect
import com.iti.linguaquest.features.auth.presentation.contract.OTPIntent
import com.iti.linguaquest.features.auth.presentation.ui.component.OtpContentCard
import com.iti.linguaquest.features.auth.presentation.viewmodel.OTPViewModel


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
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp, start = 24.dp, end = 24.dp)
                .align(Alignment.Center)
        ) {

            OtpContentCard(
                state = state,
                onIntent = onIntent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.lingo_mail),
                contentDescription = "Mascot holding mail",
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = (-20).dp)
            )
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