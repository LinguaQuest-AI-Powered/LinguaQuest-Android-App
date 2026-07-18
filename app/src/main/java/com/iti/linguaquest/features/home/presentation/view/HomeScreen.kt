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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.features.home.presentation.contract.HomeEffect
import com.iti.linguaquest.features.home.presentation.contract.HomeIntent
import com.iti.linguaquest.features.home.presentation.contract.HomeState
import com.iti.linguaquest.features.home.presentation.view.components.ContinueLessonCard
import com.iti.linguaquest.features.home.presentation.view.components.ExploreWorldsSection
import com.iti.linguaquest.features.home.presentation.view.components.LanguageProgressCard
import com.iti.linguaquest.features.home.presentation.viewModel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    onNavigateToDetails: (Int) -> Unit,
    onWorldMapClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        com.iti.linguaquest.core.navigation.SharedBackgroundState.showBackground = true
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeEffect.NavigateToLessonDetails -> onNavigateToDetails(effect.lessonId)
                is HomeEffect.NavigateToWorld -> { /* go to world detail screen when exists */ }
                HomeEffect.NavigateToAllWorlds -> { /* go to full worlds list screen when exists */ }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            HomeContent(
                state = state,
                onIntent = viewModel::onIntent
            )
        }

        FloatingActionButton(
            onClick = onWorldMapClick,
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
}

@Composable
fun HomeContent(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        state.languageProgress?.let { progress ->
            LanguageProgressCard(
                languageName = progress.languageName,
                level = progress.level,
                streakDays = progress.streakDays,
                progress = progress.progress,
                flagRes = progress.flagRes
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
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}