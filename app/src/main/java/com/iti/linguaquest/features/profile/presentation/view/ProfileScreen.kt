package com.iti.linguaquest.features.profile.presentation.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.features.profile.presentation.model.ProfileState
import com.iti.linguaquest.features.profile.presentation.view.components.AchievementCard
import com.iti.linguaquest.features.profile.presentation.view.components.LeaderboardRow
import com.iti.linguaquest.features.profile.presentation.view.components.LearningProgressCard
import com.iti.linguaquest.features.profile.presentation.view.components.ProfileHeader
import com.iti.linguaquest.features.profile.presentation.view.components.SectionHeader
import com.iti.linguaquest.features.profile.presentation.view.components.SettingsRow
import com.iti.linguaquest.features.profile.presentation.view.components.StatsGrid

@Composable
fun ProfileScreen(
    state: ProfileState,
    onSettingsClick: () -> Unit = {},
    onEditAvatarClick: () -> Unit = {},
    onChangeLanguageClick: () -> Unit = {},
    onViewAllAchievementsClick: () -> Unit = {},
    onViewAllLeaderboardClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ProfileContent(
        state = state,
        onSettingsClick = onSettingsClick,
        onEditAvatarClick = onEditAvatarClick,
        onChangeLanguageClick = onChangeLanguageClick,
        onViewAllAchievementsClick = onViewAllAchievementsClick,
        onViewAllLeaderboardClick = onViewAllLeaderboardClick,
        modifier = modifier
    )
}

@Composable
fun ProfileContent(
    state: ProfileState,
    onSettingsClick: () -> Unit,
    onEditAvatarClick: () -> Unit,
    onChangeLanguageClick: () -> Unit,
    onViewAllAchievementsClick: () -> Unit,
    onViewAllLeaderboardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { ProfileHeader(state, onEditAvatarClick) }
        item { StatsGrid(state) }
        item { LearningProgressCard(state, onChangeLanguageClick) }
        item { SettingsRow(onClick = onSettingsClick) }
        item {
            SectionHeader(stringResource(R.string.achievements_title), onViewAllAchievementsClick)
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.achievements, key = { it.id }) { AchievementCard(it) }
            }
        }
        item {
            SectionHeader(stringResource(R.string.leaderboard_title), onViewAllLeaderboardClick)
        }
        items(state.nearbyLeaderboard, key = { it.rank }) {
            LeaderboardRow(it)
        }
    }
}










