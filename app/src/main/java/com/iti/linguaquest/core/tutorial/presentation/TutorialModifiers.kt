package com.iti.linguaquest.core.tutorial.presentation

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import com.iti.linguaquest.core.tutorial.domain.TutorialManager

fun Modifier.tutorialTarget(
    stepId: String,
    manager: TutorialManager
): Modifier = this.onGloballyPositioned { coordinates ->
    if (coordinates.isAttached) {
        manager.registerTarget(stepId, coordinates.boundsInRoot())
    } else {
        manager.unregisterTarget(stepId)
    }
}
