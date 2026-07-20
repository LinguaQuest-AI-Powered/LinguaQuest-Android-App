package com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components
import com.iti.linguaquest.core.theme.LinguaQuestTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R


@Composable
fun CompletedDayNode() {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(LinguaQuestTheme.colors.whiteColor),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color(0xFFF9EFE6)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(id = R.string.cd_completed),
                tint = Color(0xFFD4C1B1),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
