package com.iti.linguaquest.core.tutorial.domain.model

import com.iti.linguaquest.core.tutorial.model.TargetBounds
import com.iti.linguaquest.core.tutorial.model.TutorialTour

data class TutorialState(
    val activeTour: TutorialTour? = null,
    val currentStepIndex: Int = -1,
    val targets: Map<String, TargetBounds> = emptyMap(),
    val isVisible: Boolean = false
)
