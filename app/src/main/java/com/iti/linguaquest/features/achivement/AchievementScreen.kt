package com.iti.linguaquest.features.achivement

import androidx.compose.runtime.Composable
import com.iti.linguaquest.core.mockData.achievements

import com.iti.linguaquest.core.sharedComponents.LoadingView

@Composable
fun AchievementScreen(
    onBackClick: () -> Unit,
    isLoading: Boolean = false
) {
    if (isLoading) {
        LoadingView()
    } else {
        AchievementContent(
            achievements = achievements,
            onBackClick = onBackClick,
            onClaimClick = { /* Handle claim */ }
        )
    }
}
