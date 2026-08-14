package com.iti.linguaquest.core.tutorial.domain.model

sealed interface TutorialEffect {
    data class RequestTabSwitch(val tabIndex: Int) : TutorialEffect
}
