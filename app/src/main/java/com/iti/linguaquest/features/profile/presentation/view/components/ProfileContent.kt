package com.iti.linguaquest.features.profile.presentation.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.distinctUntilChanged
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.animations.LingoEntranceAnimations
import com.iti.linguaquest.core.sharedComponents.animations.StaggeredAnimatedItem
import com.iti.linguaquest.core.sharedComponents.animations.rememberStaggeredAnimationState
import com.iti.linguaquest.core.tutorial.model.TourId
import com.iti.linguaquest.core.tutorial.presentation.LocalTutorialManager
import com.iti.linguaquest.core.tutorial.presentation.tutorialTarget
import com.iti.linguaquest.features.profile.presentation.model.ProfileState

@Composable
fun ProfileContent(
    state: ProfileState,
    isAvatarUploading: Boolean = false,
    onSettingsClick: () -> Unit,
    onEditAvatarClick: () -> Unit,
    onViewAllAchievementsClick: () -> Unit,
    onViewAllLeaderboardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animationState = rememberStaggeredAnimationState(count = 6)
    val listState = rememberLazyListState()
    val tutorialManager = LocalTutorialManager.current

    if (tutorialManager != null) {
        LaunchedEffect(tutorialManager, state.achievements.isNotEmpty()) {
            tutorialManager.state
                .map { it.activeTour to it.currentStepIndex }
                .distinctUntilChanged()
                .collect { (tour, currentStepIndex) ->
                    if (tour?.tourId == TourId.PROFILE_TOUR) {
                        val currentStep = tour.steps.getOrNull(currentStepIndex)
                        when (currentStep?.stepId) {
                            "profile_header_target", "profile_stats_target" -> {
                                listState.animateScrollToItem(index = 0)
                            }
                            "profile_achievements_target" -> {
                                listState.animateScrollToItem(index = 4)
                            }
                            "profile_leaderboard_target" -> {
                                val index = if (state.achievements.isNotEmpty()) 6 else 4
                                listState.animateScrollToItem(index = index)
                            }
                            "profile_settings_target" -> {
                                listState.animateScrollToItem(index = 3)
                            }
                        }
                    }
                }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            StaggeredAnimatedItem(
                index = 0,
                state = animationState,
                enter = LingoEntranceAnimations.popUpHorizontally(offset = 200)
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .tutorialTarget("profile_header_target")
                ) {
                    ProfileHeader(state, onEditAvatarClick, isAvatarUploading)
                }
            }
        }
        item {
            StaggeredAnimatedItem(
                index = 1,
                state = animationState,
                enter = LingoEntranceAnimations.popUpHorizontally(offset = 200)
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .tutorialTarget("profile_stats_target")
                ) {
                    StatsGrid(state)
                }
            }
        }
        item {
            StaggeredAnimatedItem(
                index = 2,
                state = animationState,
                enter = LingoEntranceAnimations.popUpHorizontally(offset = 200)
            ) {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    LearningProgressCard(state)
                }
            }
        }
        item {
            StaggeredAnimatedItem(
                index = 3,
                state = animationState,
                enter = LingoEntranceAnimations.popUpHorizontally(offset = 200)
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .tutorialTarget("profile_settings_target")
                ) {
                    SettingsRow(onClick = onSettingsClick)
                }
            }
        }

        if (state.achievements.isNotEmpty()) {
            item {
                StaggeredAnimatedItem(
                    index = 4,
                    state = animationState,
                    enter = LingoEntranceAnimations.popUpHorizontally(offset = 200)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .tutorialTarget("profile_achievements_target")
                    ) {
                        SectionHeader(
                            stringResource(R.string.achievements_title),
                            onViewAllAchievementsClick
                        )
                    }
                }
            }
            item {
                StaggeredAnimatedItem(
                    index = 4,
                    state = animationState,
                    enter = LingoEntranceAnimations.popUpHorizontally(offset = 200)
                ) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.achievements, key = { it.id }) { achievement ->
                            AchievementCard(
                                achievement = achievement,
                                modifier = Modifier.fillParentMaxWidth(0.85f)
                            )
                        }
                    }
                }
            }
        }

        if (state.nearbyLeaderboard.isNotEmpty()) {
            item {
                StaggeredAnimatedItem(
                    index = 5,
                    state = animationState,
                    enter = LingoEntranceAnimations.popUpHorizontally(offset = 200)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .tutorialTarget("profile_leaderboard_target")
                    ) {
                        SectionHeader(
                            title = stringResource(R.string.leaderboard_title),
                            onViewAllClick = onViewAllLeaderboardClick
                        )
                    }
                }
            }
            items(state.nearbyLeaderboard, key = { it.rank }) { entry ->
                StaggeredAnimatedItem(
                    index = 5,
                    state = animationState,
                    enter = LingoEntranceAnimations.popUpHorizontally(offset = 200)
                ) {
                    LeaderboardRow(
                        entry = entry,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}
