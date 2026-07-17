package com.iti.linguaquest.features.game.presentation.camera.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ImageWrapper

@Composable
fun CameraPreviewContent(
    imageUri: Uri,
    targetWord: String,
    onRetryClicked: () -> Unit,
    onSubmitClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LinguaQuestTheme.colors.blackColor)
    ) {
        ImageWrapper(
            model = imageUri,
            contentDescription = stringResource(R.string.captured_preview),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = LinguaQuestTheme.colors.blackColor.copy(alpha = 0.6f),
                contentColor = LinguaQuestTheme.colors.whiteColor
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .systemBarsPadding()
                .padding(top = 16.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.find_label) + "\n")
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = MaterialTheme.typography.headlineSmall.fontSize
                        )
                    ) {
                        append(targetWord)
                    }
                },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                color = LinguaQuestTheme.colors.whiteColor,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp, start = 32.dp, end = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = onRetryClicked,
                modifier = Modifier.size(72.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = LinguaQuestTheme.colors.socialButtonBorder,
                    contentColor = LinguaQuestTheme.colors.blackColor
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.retry),
                    modifier = Modifier.size(36.dp)
                )
            }
            IconButton(
                onClick = onSubmitClicked,
                modifier = Modifier.size(72.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = LinguaQuestTheme.colors.whiteColor
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.submit),
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}