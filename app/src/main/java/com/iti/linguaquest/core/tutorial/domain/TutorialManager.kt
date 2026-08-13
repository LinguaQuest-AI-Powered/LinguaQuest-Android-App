package com.iti.linguaquest.core.tutorial.domain

import androidx.compose.ui.geometry.Rect
import com.iti.linguaquest.R
import com.iti.linguaquest.core.tutorial.data.TutorialPreferences
import com.iti.linguaquest.core.tutorial.model.TutorialStep
import com.iti.linguaquest.core.tutorial.model.TutorialTour
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

    fun startAppTour(force: Boolean = true) {
        val appTour = TutorialTour(
            tourId = "APP_TOUR",
            steps = listOf(
                TutorialStep(
                    stepId = "tutorial_top_bar_coins",
                    titleRes = R.string.tutorial_top_bar_coins_title,
                    descriptionRes = R.string.tutorial_top_bar_coins_desc,
                    lingoImageRes = R.drawable.lingo_on_coins
                ),
                TutorialStep(
                    stepId = "tutorial_top_bar_xp",
                    titleRes = R.string.tutorial_top_bar_xp_title,
                    descriptionRes = R.string.tutorial_top_bar_xp_desc,
                    lingoImageRes = R.drawable.lingo_change_name
                ),
                TutorialStep(
                    stepId = "tutorial_top_bar_notifications",
                    titleRes = R.string.tutorial_top_bar_notifications_title,
                    descriptionRes = R.string.tutorial_top_bar_notifications_desc,
                    lingoImageRes = R.drawable.lingo_help_qw
                ),
                TutorialStep(
                    stepId = "tutorial_language_progress",
                    titleRes = R.string.tutorial_language_progress_title,
                    descriptionRes = R.string.tutorial_language_progress_desc,
                    lingoImageRes = R.drawable.lingo_level_language
                ),
                TutorialStep(
                    stepId = "tutorial_word_capture",
                    titleRes = R.string.tutorial_word_capture_title,
                    descriptionRes = R.string.tutorial_word_capture_desc,
                    lingoImageRes = R.drawable.lingo_gallery_defualt
                ),
                TutorialStep(
                    stepId = "tutorial_world_list",
                    titleRes = R.string.tutorial_world_list_title,
                    descriptionRes = R.string.tutorial_world_list_desc,
                    lingoImageRes = R.drawable.lingo_map_1
                ),
                TutorialStep(
                    stepId = "tutorial_language_button",
                    titleRes = R.string.tutorial_language_button_title,
                    descriptionRes = R.string.tutorial_language_button_desc,
                    lingoImageRes = R.drawable.lingo_onboarding_6
                ),
                TutorialStep(
                    stepId = "tutorial_daily_mission",
                    titleRes = R.string.tutorial_daily_mission_title,
                    descriptionRes = R.string.tutorial_daily_mission_desc,
                    lingoImageRes = R.drawable.lingo_reward
                )
            )
        )
        startTour(appTour, force)
    }

    fun startGalleryTour(force: Boolean = false) {
        val galleryTour = TutorialTour(
            tourId = "GALLERY_TOUR",
            steps = listOf(
                TutorialStep(
                    stepId = "gallery_tab_game_captures",
                    titleRes = R.string.tutorial_gallery_captures_title,
                    descriptionRes = R.string.tutorial_gallery_captures_desc,
                    lingoImageRes = R.drawable.lingo_gellary_icon
                ),
                TutorialStep(
                    stepId = "gallery_tab_my_journal",
                    titleRes = R.string.tutorial_gallery_journal_title,
                    descriptionRes = R.string.tutorial_gallery_journal_desc,
                    lingoImageRes = R.drawable.lingo_writing
                )
            )
        )
        startTour(galleryTour, force)
    }

    fun startLingosTour(force: Boolean = false) {
        val lingosTour = TutorialTour(
            tourId = "LINGOS_TOUR",
            steps = listOf(
                TutorialStep(
                    stepId = "lingos_card_voice",
                    titleRes = R.string.tutorial_lingos_voice_title,
                    descriptionRes = R.string.tutorial_lingos_voice_desc,
                    lingoImageRes = R.drawable.lingo_initial_state_voice
                ),
                TutorialStep(
                    stepId = "lingos_card_roleplay",
                    titleRes = R.string.tutorial_lingos_roleplay_title,
                    descriptionRes = R.string.tutorial_lingos_roleplay_desc,
                    lingoImageRes = R.drawable.lingo_hello_review
                ),
                TutorialStep(
                    stepId = "lingos_card_mindreader",
                    titleRes = R.string.tutorial_lingos_mindreader_title,
                    descriptionRes = R.string.tutorial_lingos_mindreader_desc,
                    lingoImageRes = R.drawable.lingo_mind_thinking
                )
            )
        )
        startTour(lingosTour, force)
    }

    fun startProfileTour(
        force: Boolean = false,
        hasAchievements: Boolean = true,
        hasLeaderboard: Boolean = true
    ) {
        val steps = mutableListOf(
            TutorialStep(
                stepId = "profile_header_target",
                titleRes = R.string.tutorial_profile_header_title,
                descriptionRes = R.string.tutorial_profile_header_desc,
                lingoImageRes = R.drawable.lingo_change_name
            ),
            TutorialStep(
                stepId = "profile_stats_target",
                titleRes = R.string.tutorial_profile_stats_title,
                descriptionRes = R.string.tutorial_profile_stats_desc,
                lingoImageRes = R.drawable.lingo_acheviment
            ),
            TutorialStep(
                stepId = "profile_settings_target",
                titleRes = R.string.tutorial_profile_settings_title,
                descriptionRes = R.string.tutorial_profile_settings_desc,
                lingoImageRes = R.drawable.lingo_stting
            )
        )

        if (hasAchievements) {
            steps.add(
                TutorialStep(
                    stepId = "profile_achievements_target",
                    titleRes = R.string.tutorial_profile_achievements_title,
                    descriptionRes = R.string.tutorial_profile_achievements_desc,
                    lingoImageRes = R.drawable.lingo_acheviment
                )
            )
        }

        if (hasLeaderboard) {
            steps.add(
                TutorialStep(
                    stepId = "profile_leaderboard_target",
                    titleRes = R.string.tutorial_profile_leaderboard_title,
                    descriptionRes = R.string.tutorial_profile_leaderboard_desc,
                    lingoImageRes = R.drawable.lingo_leaderboard
                )
            )
        }

        val profileTour = TutorialTour(
            tourId = "PROFILE_TOUR",
            steps = steps
        )
        startTour(profileTour, force)
    }

    fun startTour(tour: TutorialTour, force: Boolean = false) {
        scope.launch {
            if (!force) {
                val completed = preferences.isTutorialCompleted(tour.tourId).first()
                if (completed) return@launch
            } else {
                preferences.setTutorialCompleted(tour.tourId, false)
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
        // No tab-switching side effects for the home-based tour
    }
}
