package com.iti.linguaquest.features.achivement

import androidx.compose.runtime.Composable
import com.iti.linguaquest.R
import com.iti.linguaquest.core.mockData.achievements
import com.iti.linguaquest.features.achivement.model.AchievementItem

@Composable
fun AchievementScreen(
    onBackClick: () -> Unit
) {

    AchievementContent(
        achievements = achievements,
        onBackClick = onBackClick,
        onClaimClick = { /* Handle claim */ }
    )
}
