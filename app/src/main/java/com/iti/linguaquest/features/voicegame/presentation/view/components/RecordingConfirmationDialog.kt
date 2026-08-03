package com.iti.linguaquest.features.voicegame.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.AppOutlinedButton
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun RecordingConfirmationDialog(
    playbackSeconds: Int,
    isPlaying: Boolean,
    onTogglePlayback: () -> Unit,
    onDiscard: () -> Unit,
    onProcess: () -> Unit
) {
    Dialog(
        onDismissRequest = onDiscard,
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        AppMascotGradientBox(
            imageRes = R.drawable.lingo_new_password,
            mascotOverlapHeight = 60.dp,
            mascotSize = 140.dp
        ) {
            Text(
                stringResource(R.string.dialog_voice_confirmation_review_recording),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.blackColor
            )
            Spacer(Modifier.height(6.dp))
            Text(
                stringResource(R.string.dialog_voice_confirmation_sub_title),
                textAlign = TextAlign.Center,
                color = LinguaQuestTheme.colors.iconsColor,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onTogglePlayback() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play preview",
                        tint = LinguaQuestTheme.colors.whiteColor
                    )
                }
                Spacer(Modifier.width(10.dp))
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val heights = remember { listOf(10, 18, 8, 22, 14, 20, 9, 16, 12) }
                    heights.forEach { h ->
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(h.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    formatElapsed(playbackSeconds),
                    fontWeight = FontWeight.Bold,
                    color = LinguaQuestTheme.colors.blackColor
                )
            }

            Spacer(Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AppOutlinedButton(
                    text = stringResource(R.string.dialog_voice_confirmation_discard),
                    onClick = onDiscard,
                    modifier = Modifier.weight(1f),
                    color = LinguaQuestTheme.colors.iconsColor
                )
                AppButton3D(
                    text = stringResource(R.string.dialog_voice_confirmation_process),
                    onClick = onProcess,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}