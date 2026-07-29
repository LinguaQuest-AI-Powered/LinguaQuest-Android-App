package com.iti.linguaquest.features.help.presentation.help.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ImageWrapper
import com.iti.linguaquest.features.help.data.HelpTopic

@Composable
fun HelpTopicDetailCard(
    topic: HelpTopic,
    modifier: Modifier = Modifier
) {
    val accent = when (topic) {
        HelpTopic.FAQS -> LinguaQuestTheme.colors.LeaderboardGold
        HelpTopic.CONTACT_US -> LinguaQuestTheme.colors.LeaderboardBlue
        HelpTopic.USER_GUIDE -> MaterialTheme.colorScheme.tertiary
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(LinguaQuestTheme.colors.ProfileCardColor)
            .border(1.dp, accent.copy(alpha = 0.24f), RoundedCornerShape(28.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(accent.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                ImageWrapper(
                    model = topic.imageRes,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(topic.titleRes),
                    color = LinguaQuestTheme.colors.BrownText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(topic.subtitleRes),
                    color = LinguaQuestTheme.colors.iconsColor,
                    fontSize = 12.sp
                )
            }
        }

        Text(
            text = stringResource(topic.detailRes),
            color = LinguaQuestTheme.colors.BrownText,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}
