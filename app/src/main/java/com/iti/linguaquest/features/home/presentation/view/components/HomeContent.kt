package com.iti.linguaquest.features.home.presentation.view.components

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
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
import com.iti.linguaquest.features.home.presentation.contract.ContinueLevelUi
import com.iti.linguaquest.features.home.presentation.contract.HomeState

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
    val tutorialManager = LocalTutorialManager.current

    if (tutorialManager != null) {
        LaunchedEffect(tutorialManager) {
            tutorialManager.state
                .map { it.activeTour to it.currentStepIndex }
                .distinctUntilChanged()
                .collect { (tour, currentStepIndex) ->
                    if (tour?.tourId == TourId.APP_TOUR) {
                        val currentStep = tour.steps.getOrNull(currentStepIndex)
                        when (currentStep?.stepId) {
                            "tutorial_top_bar_coins", "tutorial_top_bar_xp", "tutorial_top_bar_notifications", "tutorial_language_progress" -> {
                                scrollState.animateScrollTo(0)
                            }

                            "tutorial_word_capture", "tutorial_world_list", "tutorial_language_button", "tutorial_daily_mission" -> {
                                scrollState.animateScrollTo(scrollState.maxValue)
                            }
                        }
                    }
                }
        }
    }

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
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .tutorialTarget("tutorial_language_progress")
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
                    progressText = stringResource(
                        R.string.word_capture_progress_format,
                        level.levelOrder.toString(),
                        level.totalLevels.toString()
                    ),
                    onContinueClick = { rect ->
                        onContinueLevelClick(level, rect)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .tutorialTarget("tutorial_word_capture")
                )
            } else {
                WordCaptureCard(
                    buttonText = stringResource(R.string.start_hunting),
                    worldName = stringResource(R.string.mystery_world),
                    targetWord = "\uD83E\uDD14",
                    progressText = stringResource(
                        R.string.word_capture_progress_format,
                        "\uD83E\uDD14",
                        "10"
                    ),
                    onContinueClick = { rect ->
                        onSeeMoreClick(rect)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .tutorialTarget("tutorial_word_capture")
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .tutorialTarget("tutorial_world_list")
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
