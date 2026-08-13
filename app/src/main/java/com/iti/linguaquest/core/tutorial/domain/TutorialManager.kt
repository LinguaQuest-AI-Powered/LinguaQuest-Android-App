package com.iti.linguaquest.core.tutorial.domain

import com.iti.linguaquest.core.tutorial.domain.model.TutorialEffect
import com.iti.linguaquest.core.tutorial.domain.model.TutorialState
import com.iti.linguaquest.core.tutorial.domain.repository.TutorialRepository
import com.iti.linguaquest.core.tutorial.model.TargetBounds
import com.iti.linguaquest.core.tutorial.model.TourId
import com.iti.linguaquest.core.tutorial.model.TutorialTour
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TutorialManager @Inject constructor(
    private val repository: TutorialRepository,
    private val tourRegistry: TourRegistry,
    private val scope: CoroutineScope
) {

    private val _state = MutableStateFlow(TutorialState())
    val state: StateFlow<TutorialState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<TutorialEffect>(extraBufferCapacity = 16)
    val effect: SharedFlow<TutorialEffect> = _effect.asSharedFlow()

    fun startAppTour(force: Boolean = false) {
        startTour(tourRegistry.appTour(), force)
    }

    fun startGalleryTour(force: Boolean = false) {
        startTour(tourRegistry.galleryTour(), force)
    }

    fun startLingosTour(force: Boolean = false) {
        startTour(tourRegistry.lingosTour(), force)
    }

    fun startProfileTour(
        force: Boolean = false,
        hasAchievements: Boolean = true,
        hasLeaderboard: Boolean = true
    ) {
        startTour(tourRegistry.profileTour(hasAchievements, hasLeaderboard), force)
    }

    fun startTour(tour: TutorialTour, force: Boolean = false) {
        scope.launch {
            if (!force) {
                val completed = repository.isTourCompleted(tour.tourId)
                if (completed) return@launch
            } else {
                repository.setTourCompleted(tour.tourId, false)
            }
            _state.update {
                TutorialState(
                    activeTour = tour,
                    currentStepIndex = 0,
                    isVisible = true,
                    targets = it.targets
                )
            }
        }
    }

    fun registerTarget(stepId: String, bounds: TargetBounds) {
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
        } else {
            finishTour()
        }
    }

    fun skipTour() {
        finishTour()
    }

    fun resetAllTours() {
        scope.launch {
            repository.resetAllTours()
        }
    }

    private fun finishTour() {
        val tour = _state.value.activeTour
        if (tour != null) {
            scope.launch {
                repository.setTourCompleted(tour.tourId, true)
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
}
