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
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.theme.AppTextStyles

@Composable
fun GameFailView(
    targetWord: UiText,
    isHintUsed: Boolean,
    onRetry: () -> Unit,
    onBuyHint: () -> Unit,
    onChangeWord: () -> Unit,
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
                color = LinguaQuestTheme.colors.BrownText
            )

            Spacer(modifier = Modifier.height(16.dp))

            val wordStr = targetWord.asString()
            val failMessageFormat = stringResource(R.string.game_result_fail_message_format, wordStr)
            val targetIndex = failMessageFormat.indexOf(wordStr)
            val annotatedString = buildAnnotatedString {
                if (targetIndex != -1) {
                    append(failMessageFormat.substring(0, targetIndex))
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
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
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = LinguaQuestTheme.colors.OrangeActive
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.game_result_hint_button),
                            color = LinguaQuestTheme.colors.BrownText,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            AppButton3D(
                text = stringResource(R.string.game_result_retry_camera),
                onClick = onRetry,
                variant = ButtonVariant.PRIMARY
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppButton3D(
                text = stringResource(R.string.game_result_change_word),
                onClick = onChangeWord,
                variant = ButtonVariant.SECONDARY
            )
        }
    }
}