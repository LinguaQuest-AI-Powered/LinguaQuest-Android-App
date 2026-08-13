package com.iti.linguaquest.core.tutorial.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import com.iti.linguaquest.core.tutorial.domain.TutorialManager
import com.iti.linguaquest.core.tutorial.model.TargetBounds

@Composable
fun Modifier.tutorialTarget(
    stepId: String
): Modifier {
    val manager = LocalTutorialManager.current ?: return this
    return tutorialTarget(stepId = stepId, manager = manager)
}

fun Modifier.tutorialTarget(
    stepId: String,
    manager: TutorialManager
): Modifier = this.onGloballyPositioned { coordinates ->
    if (coordinates.isAttached) {
        val rect = coordinates.boundsInRoot()
        val bounds = TargetBounds(
            left = rect.left,
            top = rect.top,
            width = rect.width,
            height = rect.height
        )
        manager.registerTarget(stepId, bounds)
    } else {
        manager.unregisterTarget(stepId)
    }
}
