package com.iti.linguaquest.features.lingos.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
import com.iti.linguaquest.core.tutorial.model.TourId
import com.iti.linguaquest.core.tutorial.presentation.LocalTutorialManager
import com.iti.linguaquest.core.tutorial.domain.model.TutorialIntent
import com.iti.linguaquest.core.tutorial.presentation.tutorialTarget
import androidx.compose.runtime.remember
import com.iti.linguaquest.features.lingos.presentation.view.components.LingoCard
import com.iti.linguaquest.features.lingos.presentation.view.components.LingoCardItem

@Composable
fun LingosScreen(
    onNavigateToVoiceGame: () -> Unit,
    onNavigateToRoleplayList: () -> Unit,
    onNavigateToMindReader: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tutorialManager = LocalTutorialManager.current

    LaunchedEffect(tutorialManager) {
        tutorialManager?.onIntent(TutorialIntent.StartLingosTour(force = false))
    }

    val scrollState = rememberScrollState()

    if (tutorialManager != null) {
        LaunchedEffect(tutorialManager) {
            tutorialManager.state
                .map { it.activeTour to it.currentStepIndex }
                .distinctUntilChanged()
                .collect { (tour, currentStepIndex) ->
                    if (tour?.tourId == TourId.LINGOS_TOUR) {
                        val currentStep = tour.steps.getOrNull(currentStepIndex)
                        when (currentStep?.stepId) {
                            "lingos_card_voice" -> scrollState.animateScrollTo(0)
                            "lingos_card_mindreader" -> scrollState.animateScrollTo(scrollState.maxValue)
                        }
                    }
                }
        }
    }

    val cards = remember {
        listOf(
            LingoCardItem(
                id = "voice",
                chipTitleRes = R.string.voice_practise,
                descriptionRes = R.string.practice_pronunciation,
                bubbleTitleRes = R.string.voice_practise,
                buttonTextRes = R.string.start_button,
                imageRes = R.drawable.lingo_mic,
                tutorialTargetId = "lingos_card_voice"
            ),
            LingoCardItem(
                id = "roleplay",
                chipTitleRes = R.string.roleplay_label,
                descriptionRes = R.string.roleplay_interactive_scenarios,
                bubbleTitleRes = R.string.roleplay_label,
                buttonTextRes = R.string.roleplay_browse_roleplays,
                imageRes = R.drawable.lingo_writing,
                tutorialTargetId = "lingos_card_roleplay"
            ),
            LingoCardItem(
                id = "mindreader",
                chipTitleRes = R.string.mind_reader_card_title,
                descriptionRes = R.string.mind_reader_card_desc,
                bubbleTitleRes = R.string.mind_reader_card_title,
                buttonTextRes = R.string.start_button,
                imageRes = R.drawable.lingo_mind_thinking,
                tutorialTargetId = "lingos_card_mindreader"
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 20.dp)
    ) {
        cards.forEachIndexed { index, card ->
            if (index > 0) {
                Spacer(modifier = Modifier.height(16.dp))
            }
            LingoCard(
                chipTitle = stringResource(id = card.chipTitleRes),
                description = stringResource(id = card.descriptionRes),
                bubbleTitle = stringResource(id = card.bubbleTitleRes),
                buttonText = stringResource(id = card.buttonTextRes),
                imageRes = card.imageRes,
                onStartClick = {
                    when (card.id) {
                        "voice" -> onNavigateToVoiceGame()
                        "roleplay" -> onNavigateToRoleplayList()
                        "mindreader" -> onNavigateToMindReader()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .tutorialTarget(card.tutorialTargetId)
            )
        }
    }
}
