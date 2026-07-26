package com.iti.linguaquest.features.achivement.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iti.linguaquest.core.mockData.achievements
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.features.achivement.presentation.viewmodel.AchievementViewModel
import com.iti.linguaquest.features.setting.presentation.SettingViewModel

@Composable
fun AchievementScreen(
    onBackClick: () -> Unit,
    viewModel: AchievementViewModel = hiltViewModel(),

    ) {
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    OfflineAwareContent(isOnline = isOnline) {
        AchievementContent(
            achievements = achievements,
            onBackClick = onBackClick,
            onClaimClick = { /* Handle claim */ }
        )
    }

}
