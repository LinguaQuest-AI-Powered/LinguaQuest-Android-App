package com.iti.linguaquest.features.game.presentation.result.view.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultUiState

@Composable
fun GameErrorView(
    state: GameResultUiState.Error,
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
            imageRes = R.drawable.lingo_sad
        ) {
            Text(
                text = stringResource(R.string.game_result_error_title),
                style = AppTextStyles.ScreenTitle,
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.BrownText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = state.errorMessage.asString(),
                style = AppTextStyles.DialogMessage,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppButton3D(
                text = stringResource(R.string.game_result_retry_connection),
                onClick = onRetry,
                variant = ButtonVariant.PRIMARY
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppButton3D(
                text = stringResource(R.string.game_result_exit_game),
                onClick = onExit,
                variant = ButtonVariant.SECONDARY
            )
        }
    }
}