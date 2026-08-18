package com.iti.linguaquest.features.mindreader.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun MindReaderActionRow(
    onTranslateClick: () -> Unit,
    onPlayAudioClick: () -> Unit,
    modifier: Modifier = Modifier,
    isTranslating: Boolean = false
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(LinguaQuestTheme.colors.MindReaderCream)
                .clickable(enabled = !isTranslating, onClick = onTranslateClick)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            if (isTranslating) {
                LingoSpinningIcon(size = 16.dp)
            } else {
                Text(
                    text = "\uD83C\uDF10",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.mind_reader_translate),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = LinguaQuestTheme.colors.BrownText
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_speaker_icon),
            contentDescription = null,
            tint = LinguaQuestTheme.colors.BrownText,
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(50))
                .background(LinguaQuestTheme.colors.MindReaderCream)
                .clickable(onClick = onPlayAudioClick)
                .padding(6.dp)
        )
    }
}
