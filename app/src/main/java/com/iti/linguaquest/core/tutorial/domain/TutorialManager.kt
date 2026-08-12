package com.iti.linguaquest.core.tutorial.domain

import androidx.compose.ui.geometry.Rect
import com.iti.linguaquest.R
import com.iti.linguaquest.core.tutorial.data.TutorialPreferences
import com.iti.linguaquest.core.tutorial.model.TutorialStep
import com.iti.linguaquest.core.tutorial.model.TutorialTour
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TutorialState(
    val activeTour: TutorialTour? = null,
    val currentStepIndex: Int = -1,
    val targets: Map<String, Rect> = emptyMap(),
    val isVisible: Boolean = false
)

sealed interface TutorialEffect {
    data class RequestTabSwitch(val tabIndex: Int) : TutorialEffect
}

class TutorialManager(
    private val preferences: TutorialPreferences
) {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val _state = MutableStateFlow(TutorialState())
    val state: StateFlow<TutorialState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<TutorialEffect>(extraBufferCapacity = 16)
    val effect: SharedFlow<TutorialEffect> = _effect.asSharedFlow()

    fun startAppTour(force: Boolean = false) {
        val appTour = TutorialTour(
            tourId = "APP_TOUR",
            steps = listOf(
                TutorialStep(
                    stepId = "bottom_nav_home",
                    titleRes = R.string.tutorial_home_title,
                    descriptionRes = R.string.tutorial_home_desc
                ),
                TutorialStep(
                    stepId = "bottom_nav_gallery",
                    titleRes = R.string.tutorial_gallery_title,
                    descriptionRes = R.string.tutorial_gallery_desc
                ),
                TutorialStep(
                    stepId = "bottom_nav_profile",
                    titleRes = R.string.tutorial_profile_title,
                    descriptionRes = R.string.tutorial_profile_desc
                )
            )
        )
        startTour(appTour, force)
    }

    fun startTour(tour: TutorialTour, force: Boolean = false) {
        scope.launch {
            if (!force) {
                val completed = preferences.isTutorialCompleted(tour.tourId).first()
                if (completed) return@launch
            }
            _state.update {
                TutorialState(
                    activeTour = tour,
                    currentStepIndex = 0,
                    isVisible = true,
                    targets = it.targets
                )
            }
            triggerSideEffectsForStep(0)
        }
    }

    fun registerTarget(stepId: String, bounds: Rect) {
        _state.update {
            it.copy(targets = it.targets + (stepId to bounds))
        }
    }

    fun unregisterTarget(stepId: String) {
        _state.update {
            it.copy(targets = it.targets - stepId)
        }
    }

    fun nextStep() {
        val currentState = _state.value
        val tour = currentState.activeTour ?: return
        val nextIndex = currentState.currentStepIndex + 1

        if (nextIndex < tour.steps.size) {
            _state.update {
                it.copy(currentStepIndex = nextIndex)
            }
            triggerSideEffectsForStep(nextIndex)
        } else {
            finishTour()
        }
    }

    fun skipTour() {
        finishTour()
    }

    fun gotIt() {
        finishTour()
    }

    fun resetAllTours() {
        scope.launch {
            preferences.resetAllTutorials()
        }
    }

    private fun finishTour() {
        val tour = _state.value.activeTour
        if (tour != null) {
            scope.launch {
                preferences.setTutorialCompleted(tour.tourId, true)
            }
        }
        _state.update {
            it.copy(
                activeTour = null,
                currentStepIndex = -1,
                isVisible = false
            )
        }
    }

    private fun triggerSideEffectsForStep(index: Int) {
        val tour = _state.value.activeTour ?: return
        if (tour.tourId == "APP_TOUR") {
            scope.launch {
                _effect.emit(TutorialEffect.RequestTabSwitch(index))
            }
        }
    }
}
