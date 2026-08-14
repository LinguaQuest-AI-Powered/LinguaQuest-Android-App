package com.iti.linguaquest.core.tutorial.domain

import com.iti.linguaquest.core.tutorial.domain.model.TutorialEffect
import com.iti.linguaquest.core.tutorial.domain.model.TutorialIntent
import com.iti.linguaquest.core.tutorial.domain.model.TutorialState
import com.iti.linguaquest.core.tutorial.domain.usecase.IsTourCompletedUseCase
import com.iti.linguaquest.core.tutorial.domain.usecase.ResetAllToursUseCase
import com.iti.linguaquest.core.tutorial.domain.usecase.SetTourCompletedUseCase
import com.iti.linguaquest.core.tutorial.model.TargetBounds
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
    private val isTourCompletedUseCase: IsTourCompletedUseCase,
    private val setTourCompletedUseCase: SetTourCompletedUseCase,
    private val resetAllToursUseCase: ResetAllToursUseCase,
    private val tourRegistry: TourRegistry,
    private val scope: CoroutineScope
) {

    private val _state = MutableStateFlow(TutorialState())
    val state: StateFlow<TutorialState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<TutorialEffect>(extraBufferCapacity = 16)
    val effect: SharedFlow<TutorialEffect> = _effect.asSharedFlow()

    fun onIntent(intent: TutorialIntent) {
        when (intent) {
            is TutorialIntent.StartAppTour -> startAppTour(intent.force)
            is TutorialIntent.StartGalleryTour -> startGalleryTour(intent.force)
            is TutorialIntent.StartLingosTour -> startLingosTour(intent.force)
            is TutorialIntent.StartProfileTour -> startProfileTour(
                intent.force,
                intent.hasAchievements,
                intent.hasLeaderboard
            )
            is TutorialIntent.StartTour -> startTour(intent.tour, intent.force)
            is TutorialIntent.RegisterTarget -> registerTarget(intent.stepId, intent.bounds)
            is TutorialIntent.UnregisterTarget -> unregisterTarget(intent.stepId)
            TutorialIntent.NextStep -> nextStep()
            TutorialIntent.SkipTour -> skipTour()
            TutorialIntent.ResetAllTours -> resetAllTours()
        }
    }

    private fun startAppTour(force: Boolean = false) {
        startTour(tourRegistry.appTour(), force)
    }

    private fun startGalleryTour(force: Boolean = false) {
        startTour(tourRegistry.galleryTour(), force)
    }

    private fun startLingosTour(force: Boolean = false) {
        startTour(tourRegistry.lingosTour(), force)
    }

    private fun startProfileTour(
        force: Boolean = false,
        hasAchievements: Boolean = true,
        hasLeaderboard: Boolean = true
    ) {
        startTour(tourRegistry.profileTour(hasAchievements, hasLeaderboard), force)
    }

    private fun startTour(tour: TutorialTour, force: Boolean = false) {
        scope.launch {
            if (!force) {
                val completed = isTourCompletedUseCase(tour.tourId)
                if (completed) return@launch
            } else {
                setTourCompletedUseCase(tour.tourId, false)
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

    private fun registerTarget(stepId: String, bounds: TargetBounds) {
        _state.update {
            it.copy(targets = it.targets + (stepId to bounds))
        }
    }

    private fun unregisterTarget(stepId: String) {
        _state.update {
            it.copy(targets = it.targets - stepId)
        }
    }

    private fun nextStep() {
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

    private fun skipTour() {
        finishTour()
    }

    private fun resetAllTours() {
        scope.launch {
            resetAllToursUseCase()
        }
    }

    private fun finishTour() {
        val tour = _state.value.activeTour
        if (tour != null) {
            scope.launch {
                setTourCompletedUseCase(tour.tourId, true)
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
