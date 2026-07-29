package com.iti.linguaquest.features.help.presentation.guide.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ImageWrapper

@Composable
fun UserGuideHeroSection(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp, vertical = 22.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.help_guide_title),
                    color = LinguaQuestTheme.colors.BrownText,
                    fontSize = 28.sp,
                    lineHeight = 31.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth(0.34f)
                        .clip(RoundedCornerShape(50))
                        .background(LinguaQuestTheme.colors.LeaderboardBlue)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.help_guide_subtitle),
                    color = LinguaQuestTheme.colors.iconsColor,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

            ImageWrapper(
                model = R.drawable.lingo_help_a,
                contentDescription = null,
                modifier = Modifier.size(228.dp)
            )
        }
    }
}
