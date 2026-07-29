package com.iti.linguaquest.features.help.presentation.help.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ImageWrapper
import com.iti.linguaquest.features.help.data.HelpTopic

@Composable
fun HelpTopicCard(
    topic: HelpTopic,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = when (topic) {
        HelpTopic.FAQS -> LinguaQuestTheme.colors.LeaderboardGold
        HelpTopic.CONTACT_US -> LinguaQuestTheme.colors.LeaderboardBlue
        HelpTopic.USER_GUIDE -> MaterialTheme.colorScheme.tertiary
    }

    val cardBackground = accent.copy(alpha = if (isSelected) 0.18f else 0.10f)
    val borderColor = accent.copy(alpha = if (isSelected) 0.55f else 0.25f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(cardBackground)
            .border(1.2.dp, borderColor, RoundedCornerShape(26.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(82.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(accent.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            ImageWrapper(
                model = topic.imageRes,
                contentDescription = null,
                modifier = Modifier.size(72.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(topic.titleRes),
                color = accent,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(topic.subtitleRes),
                color = LinguaQuestTheme.colors.iconsColor,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_right),
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
