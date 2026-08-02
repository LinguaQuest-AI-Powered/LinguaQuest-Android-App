package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.iti.linguaquest.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme

import com.iti.linguaquest.core.sharedComponents.text.UiText

@Composable
fun BossErrorView(
    errorMessage: UiText,
    onRetry: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AppMascotGradientBox(
            imageRes = R.drawable.lingo_error,
            mascotSize = 180.dp
        ) {
            Text(
                text = stringResource(R.string.roleplay_oops),
                style = AppTextStyles.ScreenTitle,
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.BrownText,
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = errorMessage.asString(),
                style = AppTextStyles.DialogMessage,
                color = LinguaQuestTheme.colors.BrownText,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppButton3D(
                text = stringResource(R.string.roleplay_try_again),
                onClick = onRetry
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            AppButton3D(
                text = stringResource(R.string.roleplay_exit),
                onClick = onExit,
                variant = ButtonVariant.SECONDARY
            )
        }
    }
}
