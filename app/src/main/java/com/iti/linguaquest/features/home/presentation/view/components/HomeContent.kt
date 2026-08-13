package com.iti.linguaquest.features.home.presentation.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.animations.LingoEntranceAnimations
import com.iti.linguaquest.core.sharedComponents.animations.StaggeredAnimatedItem
import com.iti.linguaquest.core.sharedComponents.animations.rememberStaggeredAnimationState
import com.iti.linguaquest.features.home.presentation.contract.ContinueLevelUi
import com.iti.linguaquest.features.home.presentation.contract.HomeState
import kotlinx.coroutines.delay

@Composable
fun HomeContent(
    state: HomeState,
    onSeeMoreClick: (Rect) -> Unit,
    onWorldClick: (WorldItem, Rect) -> Unit,
    onContinueLevelClick: (ContinueLevelUi, Rect) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState()
) {
    val animationState = rememberStaggeredAnimationState(count = 3)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        state.languageProgress?.let { progress ->
            StaggeredAnimatedItem(
                index = 0,
                state = animationState,
                enter = LingoEntranceAnimations.popUpVertically(offset = -60)
            ) {
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
        }

        StaggeredAnimatedItem(
            index = 1,
            state = animationState,
            enter = LingoEntranceAnimations.popUpHorizontally(offset = 200)
        ) {
            if (state.continueLevel != null) {
                val level = state.continueLevel
                WordCaptureCard(
                    worldName = level.worldName.asString(),
                    targetWord = level.targetWord.asString(),
                    progressText = stringResource(R.string.word_capture_progress_format, level.levelOrder.toString(), level.totalLevels.toString()),
                    onContinueClick = { rect ->
                        onContinueLevelClick(level, rect)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            } else {
                WordCaptureCard(
                    buttonText = stringResource(R.string.start_hunting),
                    worldName = stringResource(R.string.mystery_world),
                    targetWord = "\uD83E\uDD14",
                    progressText = stringResource(R.string.word_capture_progress_format, "\uD83E\uDD14", "10"),
                    onContinueClick = { rect ->
                        onSeeMoreClick(rect)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (state.worlds.isNotEmpty()) {
            StaggeredAnimatedItem(
                index = 2,
                state = animationState,
                enter = LingoEntranceAnimations.popUpVertically(offset = 60)
            ) {
                ExploreWorldsSection(
                    worlds = state.worlds,
                    onSeeMoreClick = onSeeMoreClick,
                    onWorldClick = onWorldClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
