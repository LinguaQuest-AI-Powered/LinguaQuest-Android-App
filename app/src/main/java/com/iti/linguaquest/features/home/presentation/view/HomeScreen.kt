package com.iti.linguaquest.features.home.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.navigation.SharedBackgroundState
import com.iti.linguaquest.features.home.presentation.contract.HomeEffect
import com.iti.linguaquest.features.home.presentation.contract.HomeIntent
import com.iti.linguaquest.features.home.presentation.contract.HomeState
import com.iti.linguaquest.features.home.presentation.languages.component.MyLanguagesBottomSheet
import com.iti.linguaquest.features.home.presentation.languages.contract.MyLanguageUiModel
import com.iti.linguaquest.features.home.presentation.view.components.ContinueLessonCard
import com.iti.linguaquest.features.home.presentation.view.components.ExploreWorldsSection
import com.iti.linguaquest.features.home.presentation.view.components.LanguageProgressCard
import com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components.DailyRewardCard
import com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components.DailyStreakBonusBanner
import com.iti.linguaquest.features.home.presentation.viewModel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetails: (Int) -> Unit,
    onNavigateToAllWorlds: () -> Unit,
    onNavigateToWorldMap: (Int) -> Unit,
    onWorldMapClick: () -> Unit = {},
    onNavigateToAddLanguages: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDailyRewardDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        SharedBackgroundState.showBackground = true
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeEffect.NavigateToLessonDetails -> onNavigateToDetails(effect.lessonId)
                is HomeEffect.NavigateToWorld -> {
                    onNavigateToWorldMap(effect.worldId)
                }
                HomeEffect.NavigateToAllWorlds -> onNavigateToAllWorlds()
                is HomeEffect.NavigateToAddLanguages -> onNavigateToAddLanguages()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            HomeContent(
                state = state,
                onIntent = viewModel::onIntent,
                onDailyRewardClick = { showDailyRewardDialog = true }
            )
        }

        FloatingActionButton(
            onClick = {viewModel.onIntent(HomeIntent.FabClicked)},
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.world_home_icon),
                contentDescription = "world_map_content_description",
                modifier = Modifier.size(28.dp)
            )
        }
    }

    if (showDailyRewardDialog) {
        Dialog(
            onDismissRequest = { showDailyRewardDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                DailyRewardCard(
                    currentDay = 3,
                    rewardAmount = 50,
                    onClaimClick = {
                        // TODO: wire real claim logic once daily-reward endpoint/domain exists
                        showDailyRewardDialog = false
                    }
                )
            }
        }
    }

    if (state.isLanguageBottomSheetVisible) {
        MyLanguagesBottomSheet(
            languages = listOf(
                MyLanguageUiModel(1, "Spanish", 12, true, "🇪🇸"),
                MyLanguageUiModel(2, "French", 4, false, "🇫🇷"),
                MyLanguageUiModel(3, "Japanese", 3, false, "🇯🇵")
            ),
            onDismiss = { viewModel.onIntent(HomeIntent.DismissLanguageBottomSheet) },
            onAddNewLanguageClick = { viewModel.onIntent(HomeIntent.AddNewLanguageClicked) },
            onLanguageSelect = { selectedId ->
                // TODO: Add Intent to switch active language
                viewModel.onIntent(HomeIntent.DismissLanguageBottomSheet)
            }
        )
    }
}

@Composable
fun HomeContent(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    onDailyRewardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        DailyStreakBonusBanner(
            onClick = onDailyRewardClick,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        state.languageProgress?.let { progress ->
            LanguageProgressCard(
                languageName = progress.languageName,
                level = progress.level,
                streakDays = progress.streakDays,
                progress = progress.progress,
                flagSource = progress.flagSource,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        ExploreWorldsSection(
            worlds = state.worlds,
            onSeeMoreClick = { onIntent(HomeIntent.SeeMoreWorldsClicked) },
            onWorldClick = { world -> onIntent(HomeIntent.WorldClicked(world)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        state.continueLesson?.let { lesson ->
            ContinueLessonCard(
                lesson = lesson,
                onContinueClick = { onIntent(HomeIntent.ContinueLessonClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}