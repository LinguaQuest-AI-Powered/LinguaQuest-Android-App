package com.iti.linguaquest.features.profile.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.features.profile.presentation.contract.ProfileEffect
import com.iti.linguaquest.features.profile.presentation.contract.ProfileIntent
import com.iti.linguaquest.features.profile.presentation.model.ProfileState
import com.iti.linguaquest.features.profile.presentation.view.components.AchievementCard
import com.iti.linguaquest.features.profile.presentation.view.components.LeaderboardRow
import com.iti.linguaquest.features.profile.presentation.view.components.LearningProgressCard
import com.iti.linguaquest.features.profile.presentation.view.components.ProfileHeader
import com.iti.linguaquest.features.profile.presentation.view.components.SectionHeader
import com.iti.linguaquest.features.profile.presentation.view.components.SettingsRow
import com.iti.linguaquest.features.profile.presentation.view.components.StatsGrid
import com.iti.linguaquest.features.profile.presentation.viewModel.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    onSettingsClick: () -> Unit = {},
    onEditAvatarClick: () -> Unit = {},
    onChangeLanguageClick: () -> Unit = {},
    onViewAllAchievementsClick: () -> Unit = {},
    onViewAllLeaderboardClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        com.iti.linguaquest.core.navigation.SharedBackgroundState.showBackground = false
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ProfileEffect.NavigateToSettings -> onSettingsClick()
                ProfileEffect.NavigateToEditAvatar -> onEditAvatarClick()
                ProfileEffect.NavigateToChangeLanguage -> onChangeLanguageClick()
                ProfileEffect.NavigateToAllAchievements -> onViewAllAchievementsClick()
                ProfileEffect.NavigateToAllLeaderboard -> onViewAllLeaderboardClick()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            ProfileContent(
                state = uiState.profile,
                onSettingsClick = { viewModel.onIntent(ProfileIntent.SettingsClicked) },
                onEditAvatarClick = { viewModel.onIntent(ProfileIntent.EditAvatarClicked) },
                onChangeLanguageClick = { viewModel.onIntent(ProfileIntent.ChangeLanguageClicked) },
                onViewAllAchievementsClick = { viewModel.onIntent(ProfileIntent.ViewAllAchievementsClicked) },
                onViewAllLeaderboardClick = { viewModel.onIntent(ProfileIntent.ViewAllLeaderboardClicked) },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
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
        item { SectionHeader(stringResource(R.string.achievements_title), onViewAllAchievementsClick) }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.achievements, key = { it.id }) { AchievementCard(it) }
            }
        }
        item { SectionHeader(stringResource(R.string.leaderboard_title), onViewAllLeaderboardClick) }
        items(state.nearbyLeaderboard, key = { it.rank }) { LeaderboardRow(it) }
    }
}
