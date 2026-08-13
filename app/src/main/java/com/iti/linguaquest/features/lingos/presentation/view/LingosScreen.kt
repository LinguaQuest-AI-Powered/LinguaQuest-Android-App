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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.features.lingos.presentation.view.components.MindReaderCard
import com.iti.linguaquest.features.lingos.presentation.view.components.RoleplayCard
import com.iti.linguaquest.features.lingos.presentation.view.components.VoicePractiseCard
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.core.tutorial.domain.TutorialManager
import com.iti.linguaquest.core.tutorial.domain.TutorialState
import com.iti.linguaquest.core.tutorial.model.TutorialTour
import com.iti.linguaquest.core.tutorial.model.TutorialStep
import com.iti.linguaquest.core.tutorial.presentation.tutorialTarget

@Composable
fun LingosScreen(
    onNavigateToVoiceGame: () -> Unit,
    onNavigateToRoleplayList: () -> Unit,
    onNavigateToMindReader: () -> Unit,
    modifier: Modifier = Modifier,
    tutorialManager: TutorialManager? = null
) {
    LaunchedEffect(tutorialManager) {
        tutorialManager?.startLingosTour(force = false)
    }

    val scrollState = rememberScrollState()

    if (tutorialManager != null) {
        val tutorialState by tutorialManager.state.collectAsStateWithLifecycle()
        LaunchedEffect(tutorialState.currentStepIndex) {
            val tour = tutorialState.activeTour
            if (tour?.tourId == "LINGOS_TOUR") {
                val currentStep = tour.steps.getOrNull(tutorialState.currentStepIndex)
                if (currentStep?.stepId == "lingos_card_mindreader") {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 20.dp)
    ) {
        val voiceModifier = if (tutorialManager != null) {
            Modifier.tutorialTarget("lingos_card_voice", tutorialManager)
        } else {
            Modifier
        }
        VoicePractiseCard(
            onStartClick = { _ -> onNavigateToVoiceGame() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .then(voiceModifier)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val roleplayModifier = if (tutorialManager != null) {
            Modifier.tutorialTarget("lingos_card_roleplay", tutorialManager)
        } else {
            Modifier
        }
        RoleplayCard(
            onStartClick = { _ -> onNavigateToRoleplayList() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .then(roleplayModifier)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val mindReaderModifier = if (tutorialManager != null) {
            Modifier.tutorialTarget("lingos_card_mindreader", tutorialManager)
        } else {
            Modifier
        }
        MindReaderCard(
            onStartClick = { _ -> onNavigateToMindReader() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .then(mindReaderModifier)
        )
    }
}
