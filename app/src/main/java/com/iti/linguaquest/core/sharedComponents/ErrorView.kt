package com.iti.linguaquest.core.sharedComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.sharedComponents.text.UiText

@Composable
fun ErrorView(
    message: UiText,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    title: String? = null,
    retryText: String = stringResource(R.string.retry),
    onDismissRequest: (() -> Unit)? = null
) {
    ErrorView(
        message = message.asString(),
        onRetry = onRetry,
        modifier = modifier,
        title = title,
        retryText = retryText,
        onDismissRequest = onDismissRequest
    )
}

@Composable
fun ErrorView(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    title: String? = null,
    retryText: String = stringResource(R.string.retry),
    onDismissRequest: (() -> Unit)? = null
) {
    Dialog(
        onDismissRequest = { onDismissRequest?.invoke() },
        properties = DialogProperties(
            dismissOnBackPress = onDismissRequest != null,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth(0.88f)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            AppMascotGradientBox(
                imageRes = R.drawable.lingo_sad
            ) {
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = LinguaQuestTheme.colors.BrownText,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )

                if (onRetry != null) {
                    Spacer(modifier = Modifier.height(24.dp))
                    AppButton3D(
                        text = retryText,
                        onClick = onRetry,
                        variant = ButtonVariant.PRIMARY
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ErrorViewPreview() {
    ErrorView(
        message = "Something went wrong. Please check your internet connection.",
        onRetry = {}
    )
}
