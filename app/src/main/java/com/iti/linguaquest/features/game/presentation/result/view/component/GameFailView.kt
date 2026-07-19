package com.iti.linguaquest.features.game.presentation.result.view.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.AppOutlinedButton
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultUiState

@Composable
fun GameFailView(
    state: GameResultUiState.Failure,
    targetWord: com.iti.linguaquest.core.sharedComponents.text.UiText,
    isHintUsed: Boolean,
    onRetry: () -> Unit,
    onBuyHint: () -> Unit,
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
                text = stringResource(R.string.game_result_fail_title),
                style = AppTextStyles.ScreenTitle,
                fontWeight = FontWeight.Bold,
                color = AppColors.BrownText
            )

            Spacer(modifier = Modifier.height(16.dp))

            val wordStr = targetWord.asString()
            val failMessageFormat = stringResource(R.string.game_result_fail_message_format, wordStr)
            val targetIndex = failMessageFormat.indexOf(wordStr)
            val annotatedString = buildAnnotatedString {
                if (targetIndex != -1) {
                    append(failMessageFormat.substring(0, targetIndex))
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(targetWord.asString())
                        append(wordStr)
                    }
                    append(failMessageFormat.substring(targetIndex + wordStr.length))
                } else {
                    append(failMessageFormat)
                }
            }

            Text(
                text = annotatedString,

            )

            Spacer(modifier = Modifier.height(32.dp))

            if (!isHintUsed) {
                AppButton(
                    text = stringResource(R.string.game_result_hint_button),
                    onClick = onBuyHint,
                    variant = ButtonVariant.SECONDARY,
                    contentColorOverride = AppColors.BrownText,
                    icon = rememberVectorPainter(Icons.Default.Lightbulb),
                    iconPosition = IconPosition.END
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            AppButton(
                text = stringResource(R.string.game_result_retry_camera),
                onClick = onRetry,
                variant = ButtonVariant.PRIMARY
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppOutlinedButton(
                text = stringResource(R.string.game_result_change_word),
                onClick = onExit
            )
        }
    }
}